package com.hackathon.backend.domain.match.repository;

import com.hackathon.backend.domain.match.entity.MatchPair;
import com.hackathon.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchPairRepository extends JpaRepository<MatchPair, Long> {

    /**
     * 두 회원 간 MatchPair 존재 여부 확인
     */
    @Query("SELECT CASE WHEN COUNT(mp) > 0 THEN true ELSE false END FROM MatchPair mp " +
           "WHERE (mp.member1 = :member1 AND mp.member2 = :member2) " +
           "OR (mp.member1 = :member2 AND mp.member2 = :member1)")
    boolean existsByMembers(@Param("member1") Member member1, @Param("member2") Member member2);

    /**
     * 두 회원 간 MatchPair 조회
     */
    @Query("SELECT mp FROM MatchPair mp " +
           "WHERE (mp.member1 = :member1 AND mp.member2 = :member2) " +
           "OR (mp.member1 = :member2 AND mp.member2 = :member1)")
    Optional<MatchPair> findByMembers(@Param("member1") Member member1, @Param("member2") Member member2);

    /**
     * 특정 회원의 모든 MatchPair 조회
     */
    @Query("SELECT mp FROM MatchPair mp WHERE mp.member1 = :member OR mp.member2 = :member")
    List<MatchPair> findByMember(@Param("member") Member member);
}

