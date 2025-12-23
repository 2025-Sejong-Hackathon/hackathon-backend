package com.hackathon.backend.domain.match.repository;

import com.hackathon.backend.domain.match.entity.MatchPair;
import com.hackathon.backend.domain.match.entity.RoommateMatch;
import com.hackathon.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoommateMatchRepository extends JpaRepository<RoommateMatch, Long> {

    /**
     * MatchPair에 대한 RoommateMatch 조회
     */
    Optional<RoommateMatch> findByMatchPair(MatchPair matchPair);

    /**
     * 특정 회원의 모든 RoommateMatch 조회
     */
    @Query("SELECT rm FROM RoommateMatch rm WHERE rm.member1 = :member OR rm.member2 = :member")
    List<RoommateMatch> findByMember(@Param("member") Member member);

    /**
     * 특정 회원이 이미 매칭 완료되었는지 확인
     */
    @Query("SELECT CASE WHEN COUNT(rm) > 0 THEN true ELSE false END FROM RoommateMatch rm " +
           "WHERE (rm.member1 = :member OR rm.member2 = :member) " +
           "AND rm.status = 'COMPLETED'")
    boolean existsCompletedMatchByMember(@Param("member") Member member);
}

