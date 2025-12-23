package com.hackathon.backend.domain.member.service;

import com.hackathon.backend.domain.member.entity.Gender;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.hackathon.backend.global.exception.BusinessException;
import com.hackathon.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public boolean isNewMember(String studentId) {
        return memberRepository.findByStudentId(studentId).isEmpty();
    }

    public Member createOrUpdateMember(com.chuseok22.sejongportallogin.core.SejongMemberInfo sejongMemberInfo) {
        return memberRepository.findByStudentId(sejongMemberInfo.getStudentId())
                .orElseGet(() -> {
                    // 신규 회원 생성
                    Member newMember = Member.builder()
                            .studentId(sejongMemberInfo.getStudentId())
                            .name(sejongMemberInfo.getName())
                            .major(sejongMemberInfo.getMajor())
                            .grade(sejongMemberInfo.getGrade())
                            .completedSemester(sejongMemberInfo.getCompletedSemester())
                            .build();
                    memberRepository.save(newMember);
                    return newMember;
                });
    }

    /**
     * 회원 온보딩 정보 업데이트
     */
    @Transactional
    public Member updateOnboardingInfo(Long memberId, Gender gender, LocalDate birthDate,
                                       Boolean isSmoker, Boolean isDrinker,
                                       Boolean isColdSensitive, Boolean isHeatSensitive) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateOnboardingInfo(gender, birthDate, isSmoker, isDrinker, isColdSensitive, isHeatSensitive);
        return member;
    }

    /**
     * 회원 전체 정보 업데이트
     */
    @Transactional
    public Member updateMemberInfo(Long memberId, String major, String grade, String completedSemester,
                                   Gender gender, LocalDate birthDate, Boolean isSmoker, Boolean isDrinker,
                                   Boolean isColdSensitive, Boolean isHeatSensitive) {
        log.info("회원 정보 업데이트: memberId={}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateFullInfo(major, grade, completedSemester, gender, birthDate,
                isSmoker, isDrinker, isColdSensitive, isHeatSensitive);

        return member;
    }

    /**
     * 회원 조회
     */
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }
}

