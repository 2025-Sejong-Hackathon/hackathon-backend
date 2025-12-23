package com.hackathon.backend.domain.match.repository;

import com.hackathon.backend.domain.match.entity.Pick;
import com.hackathon.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PickRepository extends JpaRepository<Pick, Long> {

    /**
     * 특정 회원이 다른 회원을 pick했는지 확인
     */
    boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);

    /**
     * 특정 회원이 pick한 목록 조회
     */
    List<Pick> findByFromMember(Member fromMember);

    /**
     * 특정 회원을 pick한 목록 조회
     */
    List<Pick> findByToMember(Member toMember);

    /**
     * 두 회원 간 서로 pick 여부 확인
     */
    @Query("SELECT CASE WHEN COUNT(p) = 2 THEN true ELSE false END FROM Pick p " +
           "WHERE (p.fromMember = :member1 AND p.toMember = :member2) " +
           "OR (p.fromMember = :member2 AND p.toMember = :member1)")
    boolean existsMutualPick(@Param("member1") Member member1, @Param("member2") Member member2);

    /**
     * 특정 Pick 조회
     */
    Optional<Pick> findByFromMemberAndToMember(Member fromMember, Member toMember);
}

