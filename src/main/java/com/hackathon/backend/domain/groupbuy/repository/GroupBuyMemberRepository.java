package com.hackathon.backend.domain.groupbuy.repository;

import com.hackathon.backend.domain.groupbuy.entity.GroupBuy;
import com.hackathon.backend.domain.groupbuy.entity.GroupBuyMember;
import com.hackathon.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupBuyMemberRepository extends JpaRepository<GroupBuyMember, Long> {

    /**
     * 공동구매에 참여 중인 회원 목록 조회
     */
    List<GroupBuyMember> findByGroupBuy(GroupBuy groupBuy);

    /**
     * 공동구매에 참여 중인 회원 목록 조회 (fetch join)
     */
    @Query("SELECT gbm FROM GroupBuyMember gbm " +
           "LEFT JOIN FETCH gbm.member " +
           "LEFT JOIN FETCH gbm.groupBuy " +
           "WHERE gbm.groupBuy = :groupBuy")
    List<GroupBuyMember> findByGroupBuyWithMember(@Param("groupBuy") GroupBuy groupBuy);

    /**
     * 특정 회원이 참여한 공동구매 목록 조회
     */
    List<GroupBuyMember> findByMember(Member member);

    /**
     * 특정 회원이 참여한 공동구매 목록 조회 (fetch join)
     */
    @Query("SELECT gbm FROM GroupBuyMember gbm " +
           "LEFT JOIN FETCH gbm.member " +
           "LEFT JOIN FETCH gbm.groupBuy gb " +
           "LEFT JOIN FETCH gb.category " +
           "LEFT JOIN FETCH gb.member " +
           "WHERE gbm.member = :member")
    List<GroupBuyMember> findByMemberWithDetails(@Param("member") Member member);

    /**
     * 특정 회원이 특정 공동구매에 참여했는지 확인
     */
    boolean existsByGroupBuyAndMember(GroupBuy groupBuy, Member member);

    /**
     * 특정 공동구매의 참여자 조회
     */
    Optional<GroupBuyMember> findByGroupBuyAndMember(GroupBuy groupBuy, Member member);

    /**
     * 특정 공동구매의 참여 인원 수 조회
     */
    @Query("SELECT COUNT(gbm) FROM GroupBuyMember gbm WHERE gbm.groupBuy = :groupBuy")
    long countByGroupBuy(@Param("groupBuy") GroupBuy groupBuy);
}

