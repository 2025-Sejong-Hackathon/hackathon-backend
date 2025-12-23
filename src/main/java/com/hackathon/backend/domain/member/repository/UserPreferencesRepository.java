package com.hackathon.backend.domain.member.repository;

import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

    /**
     * 회원의 선호도 조회
     */
    Optional<UserPreferences> findByMember(Member member);

    /**
     * 회원 ID로 선호도 조회
     */
    Optional<UserPreferences> findByMemberId(Long memberId);

    /**
     * 회원의 선호도 존재 여부 확인
     */
    boolean existsByMember(Member member);
}

