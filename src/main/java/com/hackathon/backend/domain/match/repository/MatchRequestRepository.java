package com.hackathon.backend.domain.match.repository;

import com.hackathon.backend.domain.match.entity.MatchPair;
import com.hackathon.backend.domain.match.entity.MatchRequest;
import com.hackathon.backend.domain.match.entity.MatchRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {

    /**
     * MatchPair에 대한 매칭 요청 조회
     */
    Optional<MatchRequest> findByMatchPairAndStatus(MatchPair matchPair, MatchRequestStatus status);

    /**
     * MatchPair의 모든 매칭 요청 조회
     */
    List<MatchRequest> findByMatchPair(MatchPair matchPair);

    /**
     * MatchPair에 PENDING 상태의 요청이 있는지 확인
     */
    boolean existsByMatchPairAndStatus(MatchPair matchPair, MatchRequestStatus status);
}

