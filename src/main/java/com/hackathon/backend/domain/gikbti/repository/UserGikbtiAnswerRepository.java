package com.hackathon.backend.domain.gikbti.repository;

import com.hackathon.backend.domain.gikbti.entity.UserGikbtiAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGikbtiAnswerRepository extends JpaRepository<UserGikbtiAnswer, Long> {

    /**
     * 특정 회원의 모든 GIKBTI 응답 조회
     */
    List<UserGikbtiAnswer> findByMemberId(Long memberId);

    /**
     * 특정 회원의 모든 GIKBTI 응답 삭제 (재응답 시)
     */
    void deleteByMemberId(Long memberId);

    /**
     * 특정 회원이 응답했는지 확인
     */
    boolean existsByMemberId(Long memberId);
}

