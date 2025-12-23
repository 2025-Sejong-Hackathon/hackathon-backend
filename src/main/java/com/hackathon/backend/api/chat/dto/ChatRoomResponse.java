package com.hackathon.backend.api.chat.dto;

import com.hackathon.backend.domain.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "채팅방 응답")
@Getter
@Builder
@AllArgsConstructor
public class ChatRoomResponse {

    @Schema(description = "채팅방 ID")
    private Long id;

    @Schema(description = "매치 페어 ID")
    private Long matchPairId;

    @Schema(description = "상대방 ID")
    private Long partnerId;

    @Schema(description = "상대방 이름")
    private String partnerName;

    @Schema(description = "상대방 학번")
    private String partnerStudentId;

    @Schema(description = "활성 여부")
    private Boolean isActive;

    @Schema(description = "마지막 메시지 시간")
    private LocalDateTime lastMessageAt;

    @Schema(description = "읽지 않은 메시지 수")
    private Long unreadCount;

    public static ChatRoomResponse from(ChatRoom chatRoom, Long currentMemberId, Long unreadCount) {
        // 상대방 찾기
        boolean isMe1 = chatRoom.getMatchPair().getMember1().getId().equals(currentMemberId);
        var partner = isMe1 ? chatRoom.getMatchPair().getMember2() : chatRoom.getMatchPair().getMember1();

        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .matchPairId(chatRoom.getMatchPair().getId())
                .partnerId(partner.getId())
                .partnerName(partner.getName())
                .partnerStudentId(partner.getStudentId())
                .isActive(chatRoom.getIsActive())
                .lastMessageAt(chatRoom.getUpdatedAt())
                .unreadCount(unreadCount)
                .build();
    }
}

