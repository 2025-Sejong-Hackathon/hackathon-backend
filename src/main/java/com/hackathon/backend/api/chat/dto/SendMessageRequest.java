package com.hackathon.backend.api.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "채팅 메시지 전송 요청")
@Getter
@NoArgsConstructor
public class SendMessageRequest {

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    @NotBlank(message = "메시지 내용은 필수입니다.")
    private String content;
}

