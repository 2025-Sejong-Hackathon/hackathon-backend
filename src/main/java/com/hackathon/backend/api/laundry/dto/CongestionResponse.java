package com.hackathon.backend.api.laundry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CongestionResponse {
    private LaundryMessage laundryMessage;
    private CongestionForecastResponse congestionForecastResponse;
}
