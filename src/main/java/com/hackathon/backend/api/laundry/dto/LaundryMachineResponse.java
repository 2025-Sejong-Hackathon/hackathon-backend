package com.hackathon.backend.api.laundry.dto;

import com.hackathon.backend.domain.laundry.entity.GenderZone;
import com.hackathon.backend.domain.laundry.entity.LaundryMachine;
import com.hackathon.backend.domain.laundry.entity.LaundrySession;
import com.hackathon.backend.domain.laundry.entity.MachineStatus;
import com.hackathon.backend.domain.laundry.entity.MachineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "세탁기/건조기 응답")
@Getter
@Builder
@AllArgsConstructor
public class LaundryMachineResponse {

    @Schema(description = "기계 ID")
    private Long id;

    @Schema(description = "기계 타입 (WASHER: 세탁기, DRYER: 건조기)")
    private MachineType type;

    @Schema(description = "성별 존 (MALE: 남자, FEMALE: 여자)")
    private GenderZone genderZone;

    @Schema(description = "기계 번호")
    private Integer machineNumber;

    @Schema(description = "상태 (AVAILABLE: 사용가능, IN_USE: 사용중, OUT_OF_ORDER: 고장)")
    private MachineStatus status;

    @Schema(description = "남은 시간 (초)")
    private Integer remainingSeconds;

    @Schema(description = "남은 시간 (분)")
    private Integer remainingMinutes;

    public static LaundryMachineResponse from(LaundryMachine machine, LaundrySession session) {
        int remainingSeconds = 0;

        // 사용 중인 경우 세션에서 남은 시간 계산
        if (session != null && machine.getStatus() == MachineStatus.IN_USE) {
            remainingSeconds = session.getRemainingSeconds();
        }

        return LaundryMachineResponse.builder()
                .id(machine.getId())
                .type(machine.getType())
                .genderZone(machine.getGenderZone())
                .machineNumber(machine.getMachineNumber())
                .status(machine.getStatus())
                .remainingSeconds(remainingSeconds)
                .remainingMinutes(remainingSeconds > 0 ? (int) Math.ceil(remainingSeconds / 60.0) : 0)
                .build();
    }
}

