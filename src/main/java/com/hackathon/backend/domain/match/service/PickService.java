
package com.hackathon.backend.domain.match.service;

import com.hackathon.backend.api.match.dto.*;
import com.hackathon.backend.domain.chat.service.ChatService;
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
public class PickService {

    private final PickRepository pickRepository;
    private final MatchPairRepository matchPairRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final RoommateMatchRepository roommateMatchRepository;
    private final MemberRepository memberRepository;
    private final ChatService chatService;

    /**
     * 회원 Pick
     */
    @Transactional
    public PickResponse createPick(Long fromMemberId, Long toMemberId) {
        // 자기 자신을 pick할 수 없음
        if (fromMemberId.equals(toMemberId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "자기 자신을 선택할 수 없습니다.");
        }

        Member fromMember = memberRepository.findById(fromMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Member toMember = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "선택한 회원을 찾을 수 없습니다."));

        // 이미 Pick했는지 확인
        if (pickRepository.existsByFromMemberAndToMember(fromMember, toMember)) {
            throw new BusinessException(ErrorCode.CONFLICT, "이미 선택한 회원입니다.");
        }

        // Pick 생성
        Pick pick = Pick.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
        pickRepository.save(pick);

        // 서로 Pick 여부 확인
        boolean isMutual = pickRepository.existsByFromMemberAndToMember(toMember, fromMember);

        // 서로 Pick인 경우 MatchPair 생성
        if (isMutual) {
            createMatchPair(fromMember, toMember);
        }

        return PickResponse.builder()
                .id(pick.getId())
                .fromMemberId(fromMember.getId())
                .fromMemberName(fromMember.getName())
                .toMemberId(toMember.getId())
                .toMemberName(toMember.getName())
                .isMutual(isMutual)
                .build();
    }

    /**
     * Pick 취소
     */
    @Transactional
    public void cancelPick(Long fromMemberId, Long toMemberId) {
        log.info("Pick 취소: fromMemberId={}, toMemberId={}", fromMemberId, toMemberId);

        Member fromMember = memberRepository.findById(fromMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Member toMember = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Pick pick = pickRepository.findByFromMemberAndToMember(fromMember, toMember)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Pick을 찾을 수 없습니다."));

        // MatchPair가 이미 생성되었는지 확인
        if (matchPairRepository.existsByMembers(fromMember, toMember)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 매칭이 진행 중이므로 취소할 수 없습니다.");
        }

        pickRepository.delete(pick);
    }

    /**
     * 내가 Pick한 목록 조회
     */
    public List<PickResponse> getMyPicks(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<Pick> picks = pickRepository.findByFromMember(member);

        return picks.stream()
                .map(pick -> {
                    boolean isMutual = pickRepository.existsByFromMemberAndToMember(
                            pick.getToMember(), pick.getFromMember());
                    return PickResponse.builder()
                            .id(pick.getId())
                            .fromMemberId(pick.getFromMember().getId())
                            .fromMemberName(pick.getFromMember().getName())
                            .toMemberId(pick.getToMember().getId())
                            .toMemberName(pick.getToMember().getName())
                            .isMutual(isMutual)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 나를 Pick한 목록 조회
     */
    public List<PickResponse> getPicksToMe(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<Pick> picks = pickRepository.findByToMember(member);

        return picks.stream()
                .map(pick -> {
                    boolean isMutual = pickRepository.existsByFromMemberAndToMember(
                            pick.getToMember(), pick.getFromMember());
                    return PickResponse.builder()
                            .id(pick.getId())
                            .fromMemberId(pick.getFromMember().getId())
                            .fromMemberName(pick.getFromMember().getName())
                            .toMemberId(pick.getToMember().getId())
                            .toMemberName(pick.getToMember().getName())
                            .isMutual(isMutual)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * MatchPair 생성 (서로 Pick인 경우)
     */
    private void createMatchPair(Member member1, Member member2) {
        // 이미 MatchPair가 있는지 확인
        if (matchPairRepository.existsByMembers(member1, member2)) {
            log.info("MatchPair가 이미 존재함: member1={}, member2={}", member1.getId(), member2.getId());
            return;
        }

        MatchPair matchPair = MatchPair.builder()
                .member1(member1)
                .member2(member2)
                .build();
        matchPairRepository.save(matchPair);

        log.info("MatchPair 생성 완료: id={}, member1={}, member2={}",
                matchPair.getId(), member1.getId(), member2.getId());

        // 🎉 채팅방 자동 생성
        try {
            chatService.createChatRoom(matchPair);
            log.info("채팅방 자동 생성 완료: matchPairId={}", matchPair.getId());
        } catch (Exception e) {
            log.error("채팅방 생성 실패: matchPairId={}", matchPair.getId(), e);
            // 채팅방 생성 실패해도 MatchPair는 유지
        }
    }

    /**
     * 내 MatchPair 목록 조회
     */
    public List<MatchPairResponse> getMyMatchPairs(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<MatchPair> matchPairs = matchPairRepository.findByMember(member);

        return matchPairs.stream()
                .map(mp -> MatchPairResponse.builder()
                        .id(mp.getId())
                        .member1Id(mp.getMember1().getId())
                        .member1Name(mp.getMember1().getName())
                        .member1StudentId(mp.getMember1().getStudentId())
                        .member2Id(mp.getMember2().getId())
                        .member2Name(mp.getMember2().getName())
                        .member2StudentId(mp.getMember2().getStudentId())
                        .createdAt(mp.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}