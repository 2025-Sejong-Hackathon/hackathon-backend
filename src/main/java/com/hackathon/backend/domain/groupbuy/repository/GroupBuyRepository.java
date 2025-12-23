package com.hackathon.backend.domain.groupbuy.repository;

import com.hackathon.backend.domain.groupbuy.entity.Category;
import com.hackathon.backend.domain.groupbuy.entity.GroupBuy;
import com.hackathon.backend.domain.groupbuy.entity.GroupBuyStatus;
import com.hackathon.backend.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {

    /**
     * ID로 공동구매 조회 (fetch join으로 Category, Member 함께 조회)
     */
    @Query("SELECT gb FROM GroupBuy gb " +
           "LEFT JOIN FETCH gb.category " +
           "LEFT JOIN FETCH gb.member " +
           "WHERE gb.id = :id")
    Optional<GroupBuy> findByIdWithDetails(@Param("id") Long id);

    /**
     * 카테고리별 공동구매 목록 조회
     */
    Page<GroupBuy> findByCategory(Category category, Pageable pageable);

    /**
     * 상태별 공동구매 목록 조회
     */
    Page<GroupBuy> findByStatus(GroupBuyStatus status, Pageable pageable);

    /**
     * 카테고리와 상태로 공동구매 목록 조회
     */
    Page<GroupBuy> findByCategoryAndStatus(Category category, GroupBuyStatus status, Pageable pageable);

    /**
     * 작성자로 공동구매 목록 조회
     */
    List<GroupBuy> findByMember(Member member);

    /**
     * 모든 공동구매 목록 최신순 조회
     */
    @Query("SELECT gb FROM GroupBuy gb " +
           "LEFT JOIN FETCH gb.category " +
           "LEFT JOIN FETCH gb.member " +
           "ORDER BY gb.createdAt DESC")
    List<GroupBuy> findAllWithDetails();

    /**
     * 모든 공동구매 목록 최신순 조회 (페이징) - fetch join
     */
    @Query(value = "SELECT gb FROM GroupBuy gb " +
           "LEFT JOIN FETCH gb.category " +
           "LEFT JOIN FETCH gb.member " +
           "ORDER BY gb.createdAt DESC",
           countQuery = "SELECT COUNT(gb) FROM GroupBuy gb")
    Page<GroupBuy> findAllWithDetailsPageable(Pageable pageable);

    /**
     * 카테고리별 공동구매 목록 조회 (페이징) - fetch join
     */
    @Query(value = "SELECT gb FROM GroupBuy gb " +
           "LEFT JOIN FETCH gb.category c " +
           "LEFT JOIN FETCH gb.member " +
           "WHERE c = :category " +
           "ORDER BY gb.createdAt DESC",
           countQuery = "SELECT COUNT(gb) FROM GroupBuy gb WHERE gb.category = :category")
    Page<GroupBuy> findByCategoryWithDetails(@Param("category") Category category, Pageable pageable);

    /**
     * 상태별 공동구매 목록 조회 (페이징) - fetch join
     */
    @Query(value = "SELECT gb FROM GroupBuy gb " +
           "LEFT JOIN FETCH gb.category " +
           "LEFT JOIN FETCH gb.member " +
           "WHERE gb.status = :status " +
           "ORDER BY gb.createdAt DESC",
           countQuery = "SELECT COUNT(gb) FROM GroupBuy gb WHERE gb.status = :status")
    Page<GroupBuy> findByStatusWithDetails(@Param("status") GroupBuyStatus status, Pageable pageable);

    /**
     * 작성자별 공동구매 목록 조회 - fetch join
     */
    @Query("SELECT gb FROM GroupBuy gb " +
           "LEFT JOIN FETCH gb.category " +
           "LEFT JOIN FETCH gb.member m " +
           "WHERE m = :member " +
           "ORDER BY gb.createdAt DESC")
    List<GroupBuy> findByMemberWithDetails(@Param("member") Member member);
}

