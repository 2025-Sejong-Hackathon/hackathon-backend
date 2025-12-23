package com.hackathon.backend.api.groupbuy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "공동구매 생성 요청")
@Getter
@NoArgsConstructor
public class GroupBuyCreateRequest {

    @Schema(description = "카테고리 ID", example = "3")
    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    @Schema(description = "제목", example = "치킨 공동구매 하실 분~")
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @Schema(description = "설명", example = "BBQ 황금올리브 시켜먹을 사람 구해요!")
    private String description;

    @Schema(description = "목표 인원", example = "4")
    @NotNull(message = "목표 인원은 필수입니다.")
    @Positive(message = "목표 인원은 1명 이상이어야 합니다.")
    private Integer targetCount;
}

