package com.hackathon.backend.domain.chat.repository;

import com.hackathon.backend.domain.chat.entity.ChatMessage;
import com.hackathon.backend.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm " +
           "LEFT JOIN FETCH cm.sender " +
           "WHERE cm.chatRoom = :chatRoom " +
           "ORDER BY cm.createdAt ASC")
    List<ChatMessage> findByChatRoomWithSender(@Param("chatRoom") ChatRoom chatRoom);

    long countByChatRoomAndIsReadFalse(ChatRoom chatRoom);
}

