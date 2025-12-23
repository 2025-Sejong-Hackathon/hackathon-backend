package com.hackathon.backend.api.gikbti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "GIKBTI 응답 제출 요청")
public class GikbtiSubmitRequest {

    @Schema(description = "질문별 응답 목록 (16개)", example = "[{\"questionId\": 1, \"optionId\": 1}, ...]")
    @NotEmpty(message = "응답 목록은 비어있을 수 없습니다.")
    @Size(min = 16, max = 16, message = "16개의 질문에 모두 응답해야 합니다.")
    @Valid
    private List<GikbtiAnswerDto> answers;
}

