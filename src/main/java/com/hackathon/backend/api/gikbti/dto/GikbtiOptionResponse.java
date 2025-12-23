package com.hackathon.backend.api.gikbti.dto;

import com.hackathon.backend.domain.gikbti.entity.GikbtiOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "GIKBTI 옵션 응답")
@Getter
@Builder
@AllArgsConstructor
public class GikbtiOptionResponse {

    @Schema(description = "옵션 ID")
    private Long id;

    @Schema(description = "옵션 텍스트")
    private String text;

    @Schema(description = "맞는지 아닌지")
    private Boolean isTrue;

    @Schema(description = "표시 순서")
    private Integer displayOrder;

    public static GikbtiOptionResponse from(GikbtiOption option) {
        return GikbtiOptionResponse.builder()
                .id(option.getId())
                .text(option.getText())
                .isTrue(option.getIsTrue())
                .displayOrder(option.getDisplayOrder())
                .build();
    }
}

