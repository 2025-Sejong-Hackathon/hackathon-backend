package com.hackathon.backend.api.laundry.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "AI 혼잡도 예측 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CongestionForecastResponse {

    @Schema(description = "예측 날짜")
    private String date;

    @Schema(description = "피크 시간대 메시지")
    @JsonProperty("peak_message")
    private String peakMessage;

    @Schema(description = "추천 시간대 메시지")
    @JsonProperty("recommend_message")
    private String recommendMessage;

    @Schema(description = "시간대별 혼잡도")
    private List<TimelineForecast> timeline;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimelineForecast {
        @Schema(description = "시간 (0-23)")
        private Integer hour;

        @Schema(description = "예측 혼잡도 (0-10)")
        @JsonProperty("predicted_congestion")
        private Integer predictedCongestion;
    }
}

