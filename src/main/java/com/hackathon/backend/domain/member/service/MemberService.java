package com.hackathon.backend.domain.member.service;

import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.chuseok22.sejongportallogin.core.SejongMemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createOrUpdateMember(SejongMemberInfo sejongMemberInfo) {
        String studentId = sejongMemberInfo.getStudentId();

        return memberRepository.findByStudentId(studentId)
                .map(existingMember -> {
                    log.info("기존 회원 정보 업데이트: {}", studentId);
                    existingMember.updateInfo(
                            sejongMemberInfo.getName(),
                            sejongMemberInfo.getMajor(),
                            sejongMemberInfo.getGrade(),
                            sejongMemberInfo.getCompletedSemester()
                    );
                    return existingMember;
                })
                .orElseGet(() -> {
                    log.info("신규 회원 생성: {}", studentId);
                    Member newMember = Member.builder()
                            .studentId(studentId)
                            .name(sejongMemberInfo.getName())
                            .major(sejongMemberInfo.getMajor())
                            .grade(sejongMemberInfo.getGrade())
                            .completedSemesters(sejongMemberInfo.getCompletedSemester())
                            .build();
                    return memberRepository.save(newMember);
                });
    }

    public boolean isNewMember(String studentId) {
        return !memberRepository.existsByStudentId(studentId);
    }
}
