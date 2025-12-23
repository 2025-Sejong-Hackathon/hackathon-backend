package com.hackathon.backend.domain.gikbti.repository;

import com.hackathon.backend.domain.gikbti.entity.GikbtiOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GikbtiOptionRepository extends JpaRepository<GikbtiOption, Long> {

    /**
     * 특정 질문의 옵션 조회 (표시 순서대로)
     */
    List<GikbtiOption> findByGikbtiQuestionIdOrderByDisplayOrderAsc(Long questionId);
}

