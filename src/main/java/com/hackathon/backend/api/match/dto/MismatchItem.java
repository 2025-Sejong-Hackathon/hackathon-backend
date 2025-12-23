package com.hackathon.backend.api.match.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "불일치 항목")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MismatchItem {

    @Schema(description = "카테고리", example = "정리습관")
    @JsonProperty("category")
    private String category;

    @Schema(description = "나의 값", example = "나중에 치움")
    @JsonProperty("my_value")
    private String myValue;

    @Schema(description = "상대방 값", example = "바로바로 치움")
    @JsonProperty("mate_value")
    private String mateValue;
}

