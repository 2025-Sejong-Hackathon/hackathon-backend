package com.hackathon.backend.api.gikbti.dto;

import com.hackathon.backend.domain.gikbti.entity.GikbtiCategory;
import com.hackathon.backend.domain.gikbti.entity.GikbtiQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "GIKBTI 질문 응답")
@Getter
@Builder
@AllArgsConstructor
public class GikbtiQuestionResponse {

    @Schema(description = "질문 ID")
    private Long id;

    @Schema(description = "카테고리")
    private GikbtiCategory category;

    @Schema(description = "카테고리 설명")
    private String categoryDescription;

    @Schema(description = "질문 텍스트")
    private String text;

    @Schema(description = "활성화 여부")
    private Boolean isActive;

    @Schema(description = "표시 순서")
    private Integer displayOrder;

    @Schema(description = "옵션 목록")
    private List<GikbtiOptionResponse> options;

    public static GikbtiQuestionResponse from(GikbtiQuestion question, List<GikbtiOptionResponse> options) {
        return GikbtiQuestionResponse.builder()
                .id(question.getId())
                .category(question.getCategory())
                .categoryDescription(question.getCategory().getDescription())
                .text(question.getText())
                .isActive(question.getIsActive())
                .displayOrder(question.getDisplayOrder())
                .options(options)
                .build();
    }
}

