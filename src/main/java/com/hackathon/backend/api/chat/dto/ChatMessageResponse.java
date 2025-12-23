package com.hackathon.backend.api.chat.dto;

import com.hackathon.backend.domain.chat.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "채팅 메시지 응답")
@Getter
@Builder
@AllArgsConstructor
public class ChatMessageResponse {

    @Schema(description = "메시지 ID")
    private Long id;

    @Schema(description = "채팅방 ID")
    private Long chatRoomId;

    @Schema(description = "발신자 ID")
    private Long senderId;

    @Schema(description = "발신자 이름")
    private String senderName;

    @Schema(description = "메시지 내용")
    private String content;

    @Schema(description = "읽음 여부")
    private Boolean isRead;

    @Schema(description = "전송 시간")
    private LocalDateTime sentAt;

    @Schema(description = "내가 보낸 메시지 여부")
    private Boolean isMine;

    public static ChatMessageResponse from(ChatMessage message, Long currentMemberId) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .content(message.getContent())
                .isRead(message.getIsRead())
                .sentAt(message.getCreatedAt())
                .isMine(message.getSender().getId().equals(currentMemberId))
                .build();
    }
}

