package com.hackathon.backend.domain.chat.service;

import com.hackathon.backend.api.chat.dto.ChatMessageResponse;
import com.hackathon.backend.api.chat.dto.ChatRoomResponse;
import com.hackathon.backend.domain.chat.entity.ChatMessage;
import com.hackathon.backend.domain.chat.entity.ChatRoom;
import com.hackathon.backend.domain.chat.repository.ChatMessageRepository;
import com.hackathon.backend.domain.chat.repository.ChatRoomRepository;
import com.hackathon.backend.domain.match.entity.MatchPair;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.hackathon.backend.global.exception.BusinessException;
import com.hackathon.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    /**
     * MatchPair 생성 시 채팅방 자동 생성
     */
    @Transactional
    public ChatRoom createChatRoom(MatchPair matchPair) {
        log.info("채팅방 생성: matchPairId={}", matchPair.getId());

        // 이미 채팅방이 있는지 확인
        ChatRoom existingRoom = chatRoomRepository.findByMatchPair(matchPair).orElse(null);
        if (existingRoom != null) {
            log.warn("이미 채팅방이 존재함: chatRoomId={}", existingRoom.getId());
            return existingRoom;
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .matchPair(matchPair)
                .build();

        chatRoomRepository.save(chatRoom);

        log.info("채팅방 생성 완료: chatRoomId={}", chatRoom.getId());
        return chatRoom;
    }

    /**
     * 내 채팅방 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getMyChatRooms(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<ChatRoom> chatRooms = chatRoomRepository.findByMemberWithDetails(member);

        return chatRooms.stream()
                .map(chatRoom -> {
                    long unreadCount = chatMessageRepository.countByChatRoomAndIsReadFalse(chatRoom);
                    return ChatRoomResponse.from(chatRoom, memberId, unreadCount);
                })
                .toList();
    }

    /**
     * 채팅방 메시지 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatMessages(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        // 권한 확인
        validateChatRoomAccess(chatRoom, memberId);

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomWithSender(chatRoom);

        return messages.stream()
                .map(message -> ChatMessageResponse.from(message, memberId))
                .toList();
    }

    /**
     * 메시지 전송
     */
    @Transactional
    public ChatMessageResponse sendMessage(Long chatRoomId, Long memberId, String content) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        // 권한 확인
        validateChatRoomAccess(chatRoom, memberId);

        // 메시지 생성
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(member)
                .content(content)
                .build();

        chatMessageRepository.save(message);

        log.info("메시지 전송: chatRoomId={}, senderId={}", chatRoomId, memberId);

        return ChatMessageResponse.from(message, memberId);
    }

    /**
     * 메시지 읽음 처리
     */
    @Transactional
    public void markMessagesAsRead(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        validateChatRoomAccess(chatRoom, memberId);

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomWithSender(chatRoom);

        messages.stream()
                .filter(msg -> !msg.getSender().getId().equals(memberId) && !msg.getIsRead())
                .forEach(ChatMessage::markAsRead);

        log.info("메시지 읽음 처리: chatRoomId={}, memberId={}", chatRoomId, memberId);
    }

    /**
     * 채팅방 접근 권한 확인
     */
    private void validateChatRoomAccess(ChatRoom chatRoom, Long memberId) {
        MatchPair matchPair = chatRoom.getMatchPair();
        boolean hasAccess = matchPair.getMember1().getId().equals(memberId) ||
                           matchPair.getMember2().getId().equals(memberId);

        if (!hasAccess) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "채팅방에 접근할 권한이 없습니다.");
        }
    }
}

