package com.hackathon.backend.api.laundry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "세탁기 사용 시작 요청")
@Getter
@NoArgsConstructor
public class StartLaundryRequest {

    @Schema(description = "기계 ID", example = "1")
    @NotNull(message = "기계 ID는 필수입니다.")
    private Long machineId;

    @Schema(description = "사용 시간 (분)", example = "30")
    @NotNull(message = "사용 시간은 필수입니다.")
    @Min(value = 1, message = "사용 시간은 최소 1분입니다.")
    @Max(value = 120, message = "사용 시간은 최대 120분입니다.")
    private Integer durationMinutes;
}

