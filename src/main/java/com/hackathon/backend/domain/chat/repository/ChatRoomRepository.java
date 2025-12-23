package com.hackathon.backend.domain.chat.repository;

import com.hackathon.backend.domain.chat.entity.ChatRoom;
import com.hackathon.backend.domain.match.entity.MatchPair;
import com.hackathon.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByMatchPair(MatchPair matchPair);

    @Query("SELECT cr FROM ChatRoom cr " +
           "LEFT JOIN FETCH cr.matchPair mp " +
           "LEFT JOIN FETCH mp.member1 " +
           "LEFT JOIN FETCH mp.member2 " +
           "WHERE (mp.member1 = :member OR mp.member2 = :member) " +
           "AND cr.isActive = true " +
           "ORDER BY cr.updatedAt DESC")
    List<ChatRoom> findByMemberWithDetails(@Param("member") Member member);
}

