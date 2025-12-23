package com.hackathon.backend.domain.laundry.repository;

import com.hackathon.backend.domain.laundry.entity.LaundryMachine;
import com.hackathon.backend.domain.laundry.entity.LaundrySession;
import com.hackathon.backend.domain.laundry.entity.SessionStatus;
import com.hackathon.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LaundrySessionRepository extends JpaRepository<LaundrySession, Long> {

    /**
     * 회원의 실행 중인 세션 조회
     */
    List<LaundrySession> findByMemberAndStatus(Member member, SessionStatus status);

    /**
     * 기계의 실행 중인 세션 조회
     */
    Optional<LaundrySession> findByLaundryMachineAndStatus(LaundryMachine laundryMachine, SessionStatus status);

    /**
     * 회원의 모든 세션 조회 (최신순)
     */
    List<LaundrySession> findByMemberOrderByStartedAtDesc(Member member);

    /**
     * 회원의 실행 중인 세션 조회 (fetch join)
     */
    @Query("SELECT ls FROM LaundrySession ls " +
           "LEFT JOIN FETCH ls.laundryMachine " +
           "LEFT JOIN FETCH ls.member " +
           "WHERE ls.member = :member AND ls.status = :status")
    List<LaundrySession> findByMemberAndStatusWithMachine(@Param("member") Member member, @Param("status") SessionStatus status);
}

