package com.hackathon.backend.domain.member.service;

import com.hackathon.backend.domain.member.entity.Gender;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.entity.UserPreferences;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.hackathon.backend.domain.member.repository.UserPreferencesRepository;
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
    private final UserPreferencesRepository userPreferencesRepository;

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
     * Member 테이블: 개인 정보
     * UserPreferences 테이블: 룸메이트 선호도
     */
    @Transactional
    public Member updateOnboardingInfo(Long memberId, Gender gender, LocalDate birthDate,
                                       Boolean isSmoker, Boolean roommateSmokingPref,
                                       Boolean isDrinker, Boolean roommateDrinkingPref,
                                       Boolean isColdSensitive, Boolean isHeatSensitive) {
        log.info("회원 온보딩 정보 업데이트: memberId={}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 1. Member 테이블 업데이트 (개인 정보만)
        member.updateOnboardingInfo(gender, birthDate, isSmoker, isDrinker, isColdSensitive, isHeatSensitive);

        // 2. UserPreferences 테이블 생성/업데이트 (룸메이트 선호도)
        if (roommateSmokingPref != null || roommateDrinkingPref != null) {
            UserPreferences preferences = userPreferencesRepository.findByMember(member)
                    .orElseGet(() -> {
                        // 선호도 정보가 없으면 새로 생성
                        UserPreferences newPreferences = UserPreferences.builder()
                                .member(member)
                                .roommateSmokingPref(roommateSmokingPref)
                                .roommateDrinkingPref(roommateDrinkingPref)
                                .build();
                        return userPreferencesRepository.save(newPreferences);
                    });

            // 이미 존재하는 경우 업데이트
            if (preferences.getId() != null) {
                preferences.updatePreferences(roommateSmokingPref, roommateDrinkingPref);
            }

        }

        return member;
    }

    /**
     * 회원 전체 정보 업데이트
     */
    @Transactional
    public Member updateMemberInfo(Long memberId, String major, String grade, String completedSemester,
                                   Gender gender, LocalDate birthDate, Boolean isSmoker, Boolean isDrinker,
                                   Boolean isColdSensitive, Boolean isHeatSensitive,
                                   Boolean roommateSmokingPref, Boolean roommateDrinkingPref) {
        log.info("회원 정보 업데이트: memberId={}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 1. Member 테이블 업데이트
        member.updateFullInfo(major, grade, completedSemester, gender, birthDate,
                isSmoker, isDrinker, isColdSensitive, isHeatSensitive);

        // 2. UserPreferences 테이블 업데이트
        if (roommateSmokingPref != null || roommateDrinkingPref != null) {
            UserPreferences preferences = userPreferencesRepository.findByMember(member)
                    .orElseGet(() -> {
                        UserPreferences newPreferences = UserPreferences.builder()
                                .member(member)
                                .roommateSmokingPref(roommateSmokingPref)
                                .roommateDrinkingPref(roommateDrinkingPref)
                                .build();
                        return userPreferencesRepository.save(newPreferences);
                    });

            if (preferences.getId() != null) {
                preferences.updatePreferences(roommateSmokingPref, roommateDrinkingPref);
            }
        }

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

