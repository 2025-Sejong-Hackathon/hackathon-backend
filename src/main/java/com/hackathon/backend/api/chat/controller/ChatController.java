package com.hackathon.backend.api.chat.controller;

import com.hackathon.backend.api.chat.dto.ChatMessageResponse;
import com.hackathon.backend.api.chat.dto.ChatRoomResponse;
import com.hackathon.backend.api.chat.dto.SendMessageRequest;
import com.hackathon.backend.domain.chat.service.ChatService;
import com.hackathon.backend.global.response.ApiResponse;
import com.hackathon.backend.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "채팅", description = "1:1 채팅 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @Operation(
            summary = "내 채팅방 목록 조회",
            description = """
                    매칭된 상대와의 채팅방 목록을 조회합니다.
                    
                    - Pick이 서로 매칭되어 MatchPair가 생성되면 자동으로 채팅방이 생성됩니다.
                    - 읽지 않은 메시지 수를 함께 반환합니다.
                    """
    )
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getMyChatRooms(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("채팅방 목록 조회: memberId={}", memberDetails.getMemberId());

        List<ChatRoomResponse> chatRooms = chatService.getMyChatRooms(memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(chatRooms));
    }

    @Operation(
            summary = "채팅방 메시지 조회",
            description = "특정 채팅방의 모든 메시지를 시간순으로 조회합니다."
    )
    @GetMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatMessages(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long chatRoomId) {
        log.info("채팅 메시지 조회: chatRoomId={}, memberId={}", chatRoomId, memberDetails.getMemberId());

        List<ChatMessageResponse> messages = chatService.getChatMessages(chatRoomId, memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @Operation(
            summary = "메시지 전송",
            description = "채팅방에 메시지를 전송합니다."
    )
    @PostMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long chatRoomId,
            @Valid @RequestBody SendMessageRequest request) {
        log.info("메시지 전송: chatRoomId={}, memberId={}", chatRoomId, memberDetails.getMemberId());

        ChatMessageResponse message = chatService.sendMessage(
                chatRoomId,
                memberDetails.getMemberId(),
                request.getContent()
        );

        return ResponseEntity.ok(ApiResponse.success("메시지가 전송되었습니다.", message));
    }

    @Operation(
            summary = "메시지 읽음 처리",
            description = "채팅방의 읽지 않은 메시지를 모두 읽음 처리합니다."
    )
    @PostMapping("/rooms/{chatRoomId}/read")
    public ResponseEntity<ApiResponse<Void>> markMessagesAsRead(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long chatRoomId) {
        log.info("메시지 읽음 처리: chatRoomId={}, memberId={}", chatRoomId, memberDetails.getMemberId());

        chatService.markMessagesAsRead(chatRoomId, memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success());
    }
}

