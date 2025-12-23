package com.hackathon.backend.api.gikbti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "GIKBTI 제출 응답")
@Getter
@Builder
@AllArgsConstructor
public class GikbtiSubmitResponse {

    @Schema(description = "회원 ID")
    private Long memberId;

    @Schema(description = "계산된 GIKBTI 타입", example = "MCSE")
    private String gikbtiType;

    @Schema(description = "GIKBTI 타입 설명", example = "아침형-청결형-예민형-외향형")
    private String gikbtiDescription;

    public static GikbtiSubmitResponse of(Long memberId, String gikbtiType) {
        return GikbtiSubmitResponse.builder()
                .memberId(memberId)
                .gikbtiType(gikbtiType)
                .gikbtiDescription(getGikbtiDescription(gikbtiType))
                .build();
    }

    private static String getGikbtiDescription(String gikbtiType) {
        if (gikbtiType == null || gikbtiType.length() != 4) {
            return "";
        }

        StringBuilder description = new StringBuilder();
        description.append(gikbtiType.charAt(0) == 'M' ? "아침형" : "저녁형").append("-");
        description.append(gikbtiType.charAt(1) == 'C' ? "청결형" : "자유형").append("-");
        description.append(gikbtiType.charAt(2) == 'S' ? "예민형" : "둔감형").append("-");
        description.append(gikbtiType.charAt(3) == 'E' ? "외향형" : "내향형");

        return description.toString();
    }
}

