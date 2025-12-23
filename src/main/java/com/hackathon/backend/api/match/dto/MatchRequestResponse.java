package com.hackathon.backend.api.match.dto;

import com.hackathon.backend.domain.match.entity.MatchRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "매칭 요청 응답")
@Getter
@Builder
@AllArgsConstructor
public class MatchRequestResponse {

    @Schema(description = "매칭 요청 ID")
    private Long id;

    @Schema(description = "MatchPair ID")
    private Long matchPairId;

    @Schema(description = "요청 보낸 회원 ID")
    private Long senderId;

    @Schema(description = "요청 보낸 회원 이름")
    private String senderName;

    @Schema(description = "상대방 회원 ID")
    private Long receiverId;

    @Schema(description = "상대방 회원 이름")
    private String receiverName;

    @Schema(description = "요청 상태")
    private MatchRequestStatus status;

    @Schema(description = "요청 생성일시")
    private LocalDateTime createdAt;
}

