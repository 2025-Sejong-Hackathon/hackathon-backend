package com.hackathon.backend.api.gikbti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "GIKBTI 단일 응답")
public class GikbtiAnswerDto {

    @Schema(description = "질문 ID", example = "1")
    @NotNull(message = "질문 ID는 필수입니다.")
    private Long questionId;

    @Schema(description = "선택한 옵션 ID", example = "1")
    @NotNull(message = "옵션 ID는 필수입니다.")
    private Long optionId;
}

