package com.hackathon.backend.api.laundry.dto;

import com.hackathon.backend.domain.laundry.entity.LaundrySession;
import com.hackathon.backend.domain.laundry.entity.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "세탁 세션 응답")
@Getter
@Builder
@AllArgsConstructor
public class LaundrySessionResponse {

    @Schema(description = "세션 ID")
    private Long id;

    @Schema(description = "기계 정보")
    private LaundryMachineResponse machine;

    @Schema(description = "상태")
    private SessionStatus status;

    @Schema(description = "시작 시간")
    private LocalDateTime startedAt;

    @Schema(description = "종료 예정 시간")
    private LocalDateTime endAt;

    @Schema(description = "남은 시간 (초)")
    private Integer remainingSeconds;

    @Schema(description = "남은 시간 (분)")
    private Integer remainingMinutes;

    public static LaundrySessionResponse from(LaundrySession session) {
        int remainingSeconds = session.getRemainingSeconds();
        return LaundrySessionResponse.builder()
                .id(session.getId())
                .machine(LaundryMachineResponse.from(session.getLaundryMachine(), session))
                .status(session.getStatus())
                .startedAt(session.getStartedAt())
                .endAt(session.getEndAt())
                .remainingSeconds(remainingSeconds)
                .remainingMinutes((int) Math.ceil(remainingSeconds / 60.0))
                .build();
    }
}

