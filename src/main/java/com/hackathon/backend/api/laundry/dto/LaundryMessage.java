package com.hackathon.backend.api.laundry.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "오늘의 세탁 메시지")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaundryMessage {

    @Schema(description = "오늘 세탁에 대한 메시지", example = "오늘 빨래하기 좋아요 ☀️")
    @JsonProperty("laundry_message")
    private String laundryMessage;
}
