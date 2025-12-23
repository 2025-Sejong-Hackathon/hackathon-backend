package com.hackathon.backend.api.match.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Pick 응답")
@Getter
@Builder
@AllArgsConstructor
public class PickResponse {

    @Schema(description = "Pick ID")
    private Long id;

    @Schema(description = "Pick한 회원 ID")
    private Long fromMemberId;

    @Schema(description = "Pick한 회원 이름")
    private String fromMemberName;

    @Schema(description = "Pick받은 회원 ID")
    private Long toMemberId;

    @Schema(description = "Pick받은 회원 이름")
    private String toMemberName;

    @Schema(description = "서로 Pick 여부")
    private Boolean isMutual;
}

