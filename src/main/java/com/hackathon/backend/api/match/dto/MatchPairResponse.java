package com.hackathon.backend.api.match.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "MatchPair 응답")
@Getter
@Builder
@AllArgsConstructor
public class MatchPairResponse {

    @Schema(description = "MatchPair ID")
    private Long id;

    @Schema(description = "회원1 ID")
    private Long member1Id;

    @Schema(description = "회원1 이름")
    private String member1Name;

    @Schema(description = "회원1 학번")
    private String member1StudentId;

    @Schema(description = "회원2 ID")
    private Long member2Id;

    @Schema(description = "회원2 이름")
    private String member2Name;

    @Schema(description = "회원2 학번")
    private String member2StudentId;

    @Schema(description = "매칭 생성일시")
    private LocalDateTime createdAt;
}

