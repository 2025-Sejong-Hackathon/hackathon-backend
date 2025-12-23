package com.hackathon.backend.api.match.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "AI 추천 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommendResponse {

    @Schema(description = "학번", example = "226010799")
    @JsonProperty("student_id")
    private String studentId;

    @Schema(description = "전공", example = "국방시스템공학과")
    @JsonProperty("major")
    private String major;

    @Schema(description = "매칭률", example = "85")
    @JsonProperty("match_rate")
    private Integer matchRate;

    @Schema(description = "흡연 여부")
    @JsonProperty("is_smoker")
    private Boolean isSmoker;

    @Schema(description = "음주 여부")
    @JsonProperty("is_drinker")
    private Boolean isDrinker;

    @Schema(description = "더위 민감")
    @JsonProperty("sensitive_heat")
    private Boolean sensitiveHeat;

    @Schema(description = "추위 민감")
    @JsonProperty("sensitive_cold")
    private Boolean sensitiveCold;

    @Schema(description = "일치 항목 목록")
    @JsonProperty("match_items")
    private List<String> matchItems;

    @Schema(description = "불일치 항목 목록")
    @JsonProperty("mismatch_items")
    private List<MismatchItem> mismatchItems;
}

