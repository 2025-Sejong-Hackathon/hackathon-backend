package com.hackathon.backend.domain.gikbti.repository;

import com.hackathon.backend.domain.gikbti.entity.GikbtiQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GikbtiQuestionRepository extends JpaRepository<GikbtiQuestion, Long> {

    /**
     * 활성화된 질문만 조회 (표시 순서대로)
     */
    List<GikbtiQuestion> findByIsActiveTrueOrderByDisplayOrderAsc();

    /**
     * 모든 질문 조회 (표시 순서대로)
     */
    List<GikbtiQuestion> findAllByOrderByDisplayOrderAsc();
}

