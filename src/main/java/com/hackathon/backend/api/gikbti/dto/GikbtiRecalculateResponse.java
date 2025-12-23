package com.hackathon.backend.api.gikbti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "GIKBTI 재계산 응답")
@Getter
@Builder
@AllArgsConstructor
public class GikbtiRecalculateResponse {

    @Schema(description = "총 처리된 회원 수")
    private Integer totalMembers;

    @Schema(description = "성공한 회원 수")
    private Integer successCount;

    @Schema(description = "실패한 회원 수")
    private Integer failCount;

    @Schema(description = "처리 시간 (ms)")
    private Long processingTime;
}

