package com.hackathon.backend.domain.match.service;

import com.hackathon.backend.api.match.dto.MatchRequestResponse;
import com.hackathon.backend.api.match.dto.RoommateMatchResponse;
import com.hackathon.backend.domain.match.entity.*;
import com.hackathon.backend.domain.match.repository.*;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.hackathon.backend.global.exception.BusinessException;
import com.hackathon.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchRequestService {

    private final MatchPairRepository matchPairRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final RoommateMatchRepository roommateMatchRepository;
    private final MemberRepository memberRepository;

    /**
     * 매칭 요청 생성
     */
    @Transactional
    public MatchRequestResponse createMatchRequest(Long senderId, Long matchPairId) {
        log.info("매칭 요청 생성: senderId={}, matchPairId={}", senderId, matchPairId);

        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MatchPair matchPair = matchPairRepository.findById(matchPairId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "매칭 페어를 찾을 수 없습니다."));

        // sender가 matchPair의 멤버인지 확인
        if (!matchPair.getMember1().getId().equals(senderId) &&
            !matchPair.getMember2().getId().equals(senderId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "해당 매칭 페어의 회원이 아닙니다.");
        }

        // 이미 PENDING 상태의 요청이 있는지 확인
        if (matchRequestRepository.existsByMatchPairAndStatus(matchPair, MatchRequestStatus.PENDING)) {
            throw new BusinessException(ErrorCode.CONFLICT, "이미 대기 중인 매칭 요청이 있습니다.");
        }

        // 이미 완료된 매칭이 있는지 확인
        if (roommateMatchRepository.findByMatchPair(matchPair).isPresent()) {
            throw new BusinessException(ErrorCode.CONFLICT, "이미 매칭이 완료되었습니다.");
        }

        // 상대방 확인
        Member receiver = matchPair.getMember1().getId().equals(senderId)
                ? matchPair.getMember2()
                : matchPair.getMember1();

        // 매칭 요청 생성
        MatchRequest matchRequest = MatchRequest.builder()
                .matchPair(matchPair)
                .member1(matchPair.getMember1())
                .member2(matchPair.getMember2())
                .sender(sender)
                .build();
        matchRequestRepository.save(matchRequest);

        return MatchRequestResponse.builder()
                .id(matchRequest.getId())
                .matchPairId(matchPair.getId())
                .senderId(sender.getId())
                .senderName(sender.getName())
                .receiverId(receiver.getId())
                .receiverName(receiver.getName())
                .status(matchRequest.getStatus())
                .createdAt(matchRequest.getCreatedAt())
                .build();
    }

    /**
     * 매칭 요청 수락
     */
    @Transactional
    public RoommateMatchResponse acceptMatchRequest(Long receiverId, Long matchRequestId) {
        log.info("매칭 요청 수락: receiverId={}, matchRequestId={}", receiverId, matchRequestId);

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MatchRequest matchRequest = matchRequestRepository.findById(matchRequestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "매칭 요청을 찾을 수 없습니다."));

        // receiver가 해당 요청의 수신자인지 확인
        Member expectedReceiver = matchRequest.getSender().getId().equals(matchRequest.getMember1().getId())
                ? matchRequest.getMember2()
                : matchRequest.getMember1();

        if (!expectedReceiver.getId().equals(receiverId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "해당 매칭 요청의 수신자가 아닙니다.");
        }

        // 이미 처리된 요청인지 확인
        if (matchRequest.getStatus() != MatchRequestStatus.PENDING) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 처리된 요청입니다.");
        }

        // 매칭 요청 수락
        matchRequest.accept();

        // RoommateMatch 생성
        RoommateMatch roommateMatch = RoommateMatch.builder()
                .matchPair(matchRequest.getMatchPair())
                .member1(matchRequest.getMember1())
                .member2(matchRequest.getMember2())
                .build();
        roommateMatchRepository.save(roommateMatch);

        log.info("룸메이트 매칭 완료: id={}", roommateMatch.getId());

        return RoommateMatchResponse.builder()
                .id(roommateMatch.getId())
                .matchPairId(roommateMatch.getMatchPair().getId())
                .member1Id(roommateMatch.getMember1().getId())
                .member1Name(roommateMatch.getMember1().getName())
                .member1StudentId(roommateMatch.getMember1().getStudentId())
                .member2Id(roommateMatch.getMember2().getId())
                .member2Name(roommateMatch.getMember2().getName())
                .member2StudentId(roommateMatch.getMember2().getStudentId())
                .status(roommateMatch.getStatus())
                .completedAt(roommateMatch.getCompletedAt())
                .createdAt(roommateMatch.getCreatedAt())
                .build();
    }

    /**
     * 매칭 요청 거절
     */
    @Transactional
    public void rejectMatchRequest(Long receiverId, Long matchRequestId) {
        log.info("매칭 요청 거절: receiverId={}, matchRequestId={}", receiverId, matchRequestId);

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MatchRequest matchRequest = matchRequestRepository.findById(matchRequestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "매칭 요청을 찾을 수 없습니다."));

        // receiver가 해당 요청의 수신자인지 확인
        Member expectedReceiver = matchRequest.getSender().getId().equals(matchRequest.getMember1().getId())
                ? matchRequest.getMember2()
                : matchRequest.getMember1();

        if (!expectedReceiver.getId().equals(receiverId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "해당 매칭 요청의 수신자가 아닙니다.");
        }

        // 이미 처리된 요청인지 확인
        if (matchRequest.getStatus() != MatchRequestStatus.PENDING) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 처리된 요청입니다.");
        }

        matchRequest.reject();
    }

    /**
     * 내가 받은 매칭 요청 목록 조회
     */
    public List<MatchRequestResponse> getReceivedMatchRequests(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<MatchPair> myMatchPairs = matchPairRepository.findByMember(member);

        return myMatchPairs.stream()
                .flatMap(matchPair -> matchRequestRepository.findByMatchPair(matchPair).stream())
                .filter(matchRequest -> {
                    // 내가 sender가 아닌 요청만 필터링
                    return !matchRequest.getSender().getId().equals(memberId);
                })
                .filter(matchRequest -> matchRequest.getStatus() == MatchRequestStatus.PENDING)
                .map(matchRequest -> {
                    Member receiver = matchRequest.getSender().getId().equals(matchRequest.getMember1().getId())
                            ? matchRequest.getMember2()
                            : matchRequest.getMember1();

                    return MatchRequestResponse.builder()
                            .id(matchRequest.getId())
                            .matchPairId(matchRequest.getMatchPair().getId())
                            .senderId(matchRequest.getSender().getId())
                            .senderName(matchRequest.getSender().getName())
                            .receiverId(receiver.getId())
                            .receiverName(receiver.getName())
                            .status(matchRequest.getStatus())
                            .createdAt(matchRequest.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 내 룸메이트 매칭 조회
     */
    public List<RoommateMatchResponse> getMyRoommateMatches(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<RoommateMatch> matches = roommateMatchRepository.findByMember(member);

        return matches.stream()
                .map(match -> RoommateMatchResponse.builder()
                        .id(match.getId())
                        .matchPairId(match.getMatchPair().getId())
                        .member1Id(match.getMember1().getId())
                        .member1Name(match.getMember1().getName())
                        .member1StudentId(match.getMember1().getStudentId())
                        .member2Id(match.getMember2().getId())
                        .member2Name(match.getMember2().getName())
                        .member2StudentId(match.getMember2().getStudentId())
                        .status(match.getStatus())
                        .completedAt(match.getCompletedAt())
                        .createdAt(match.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

