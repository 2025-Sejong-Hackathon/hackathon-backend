package com.hackathon.backend.api.laundry.controller;

import com.hackathon.backend.api.laundry.dto.CongestionForecastResponse;
import com.hackathon.backend.api.laundry.dto.CongestionResponse;
import com.hackathon.backend.api.laundry.dto.LaundryMachineResponse;
import com.hackathon.backend.api.laundry.dto.LaundrySessionResponse;
import com.hackathon.backend.api.laundry.dto.StartLaundryRequest;
import com.hackathon.backend.domain.laundry.entity.GenderZone;
import com.hackathon.backend.domain.laundry.entity.LaundryMachine;
import com.hackathon.backend.domain.laundry.entity.LaundrySession;
import com.hackathon.backend.domain.laundry.entity.MachineType;
import com.hackathon.backend.domain.laundry.service.LaundryService;
import com.hackathon.backend.global.response.ApiResponse;
import com.hackathon.backend.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "세탁 관리", description = "세탁기/건조기 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/laundry")
public class LaundryController {

    private final LaundryService laundryService;

    @Operation(
            summary = "세탁기 목록 조회",
            description = """
                    특정 타입과 성별 존의 세탁기 목록을 조회합니다.
                    
                    - type: WASHER(세탁기), DRYER(건조기)
                    - genderZone: MALE(남자), FEMALE(여자)
                    - 각 6대씩 총 24대
                    """
    )
    @GetMapping("/machines")
    public ResponseEntity<ApiResponse<List<LaundryMachineResponse>>> getMachines(
            @RequestParam MachineType type,
            @RequestParam GenderZone genderZone) {
        log.info("세탁기 목록 조회: type={}, zone={}", type, genderZone);

        List<LaundryMachineResponse> response = laundryService.getMachinesByTypeAndZone(type, genderZone);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "성별 존 전체 기계 조회",
            description = "특정 성별 존의 모든 세탁기와 건조기를 조회합니다."
    )
    @GetMapping("/machines/zone/{genderZone}")
    public ResponseEntity<ApiResponse<List<LaundryMachineResponse>>> getAllMachinesByZone(
            @PathVariable GenderZone genderZone) {
        log.info("성별 존 전체 기계 조회: zone={}", genderZone);

        List<LaundryMachineResponse> response = laundryService.getAllMachinesByZone(genderZone);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "세탁기 사용 시작",
            description = """
                    세탁기 사용을 시작합니다.
                    
                    - 한 번에 하나의 기계만 사용 가능
                    - 사용 시간: 1~120분
                    - 사용 중인 기계는 다른 사람이 사용 불가
                    """
    )
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<LaundrySessionResponse>> startLaundry(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Valid @RequestBody StartLaundryRequest request) {
        log.info("세탁기 사용 시작: memberId={}, machineId={}", memberDetails.getMemberId(), request.getMachineId());

        LaundrySession session = laundryService.startLaundry(
                memberDetails.getMemberId(),
                request.getMachineId(),
                request.getDurationMinutes()
        );

        LaundrySessionResponse response = LaundrySessionResponse.from(session);
        return ResponseEntity.ok(ApiResponse.success("세탁기 사용이 시작되었습니다.", response));
    }

    @Operation(
            summary = "세탁기 사용 종료",
            description = "세탁기 사용을 종료합니다."
    )
    @PostMapping("/finish/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> finishLaundry(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long sessionId) {
        log.info("세탁기 사용 종료: memberId={}, sessionId={}", memberDetails.getMemberId(), sessionId);

        laundryService.finishLaundry(memberDetails.getMemberId(), sessionId);

        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(
            summary = "내가 사용 중인 세탁기 조회",
            description = """
                    현재 사용 중인 세탁기/건조기 목록을 조회합니다.
                    
                    - 실시간 남은 시간 확인 가능
                    - 사용 중인 기계가 없으면 빈 배열 반환
                    """
    )
    @GetMapping("/my-sessions")
    public ResponseEntity<ApiResponse<List<LaundrySessionResponse>>> getMySessions(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("내가 사용 중인 세탁기 조회: memberId={}", memberDetails.getMemberId());

        List<LaundrySessionResponse> response = laundryService.getMyRunningSessions(memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "세탁 혼잡도 예측 조회",
            description = """
                    AI 서버를 통해 특정 날짜의 세탁 혼잡도를 예측합니다.
                    
                    - 오늘의 세탁 메시지도 함께 제공됩니다.
                    - AI 서버에서 시간대별 혼잡도 예측 (0-10)
                    - 피크 시간대와 추천 시간대 제공
                    - 예: 2025-12-25
                    """
    )
    @GetMapping("/congestion")
    public ResponseEntity<ApiResponse<CongestionResponse>> getCongestionForecast(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam GenderZone genderZone) {
        log.info("혼잡도 예측 조회: date={}, zone={}", date, genderZone);

        CongestionResponse response = laundryService.getCongestionForecast(date, genderZone);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

