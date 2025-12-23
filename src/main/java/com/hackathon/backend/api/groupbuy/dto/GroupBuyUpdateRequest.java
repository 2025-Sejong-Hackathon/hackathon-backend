package com.hackathon.backend.api.groupbuy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "공동구매 수정 요청")
@Getter
@NoArgsConstructor
public class GroupBuyUpdateRequest {

    @Schema(description = "제목", example = "치킨 공동구매 하실 분~")
    private String title;

    @Schema(description = "설명", example = "BBQ 황금올리브 시켜먹을 사람 구해요!")
    private String description;

    @Schema(description = "목표 인원", example = "4")
    @Positive(message = "목표 인원은 1명 이상이어야 합니다.")
    private Integer targetCount;
}

