package com.hackathon.backend.domain.laundry.service;

import com.hackathon.backend.api.laundry.dto.CongestionForecastResponse;
import com.hackathon.backend.api.laundry.dto.LaundryMachineResponse;
import com.hackathon.backend.api.laundry.dto.LaundrySessionResponse;
import com.hackathon.backend.domain.laundry.entity.*;
import com.hackathon.backend.domain.laundry.repository.LaundryCongestionForecastRepository;
import com.hackathon.backend.domain.laundry.repository.LaundryMachineRepository;
import com.hackathon.backend.domain.laundry.repository.LaundrySessionRepository;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.hackathon.backend.global.exception.BusinessException;
import com.hackathon.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LaundryService {

    private final LaundryMachineRepository machineRepository;
    private final LaundrySessionRepository sessionRepository;
    private final LaundryCongestionForecastRepository congestionRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.server.url:http://43.203.41.246:8002}")
    private String aiServerUrl;

    /**
     * 타입과 성별 존별 세탁기 목록 조회 (DTO 반환)
     */
    @Transactional(readOnly = true)
    public List<LaundryMachineResponse> getMachinesByTypeAndZone(MachineType type, GenderZone genderZone) {
        log.info("세탁기 목록 조회: type={}, zone={}", type, genderZone);
        List<LaundryMachine> machines = machineRepository.findByTypeAndGenderZoneOrderByMachineNumberAsc(type, genderZone);

        // 트랜잭션 내에서 DTO 변환 (각 기계의 세션 조회)
        return machines.stream()
                .map(machine -> {
                    // 사용 중인 경우 세션 조회
                    LaundrySession session = null;
                    if (machine.getStatus() == MachineStatus.IN_USE) {
                        session = sessionRepository.findByLaundryMachineAndStatus(machine, SessionStatus.RUNNING)
                                .orElse(null);
                    }
                    return LaundryMachineResponse.from(machine, session);
                })
                .toList();
    }

    /**
     * 성별 존별 모든 기계 조회 (DTO 반환)
     */
    @Transactional(readOnly = true)
    public List<LaundryMachineResponse> getAllMachinesByZone(GenderZone genderZone) {
        log.info("성별 존 전체 기계 조회: zone={}", genderZone);
        List<LaundryMachine> machines = machineRepository.findByGenderZone(genderZone);

        // 트랜잭션 내에서 DTO 변환 (각 기계의 세션 조회)
        return machines.stream()
                .map(machine -> {
                    // 사용 중인 경우 세션 조회
                    LaundrySession session = null;
                    if (machine.getStatus() == MachineStatus.IN_USE) {
                        session = sessionRepository.findByLaundryMachineAndStatus(machine, SessionStatus.RUNNING)
                                .orElse(null);
                    }
                    return LaundryMachineResponse.from(machine, session);
                })
                .toList();
    }

    /**
     * 세탁기 사용 시작
     */
    @Transactional
    public LaundrySession startLaundry(Long memberId, Long machineId, Integer durationMinutes) {
        log.info("세탁기 사용 시작: memberId={}, machineId={}, duration={}분", memberId, machineId, durationMinutes);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        LaundryMachine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "세탁기를 찾을 수 없습니다."));

        // 이미 사용 중인지 확인
        if (machine.getStatus() == MachineStatus.IN_USE) {
            throw new BusinessException(ErrorCode.CONFLICT, "이미 사용 중인 세탁기입니다.");
        }

        if (machine.getStatus() == MachineStatus.OUT_OF_ORDER) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "고장난 세탁기입니다.");
        }

        // 회원이 이미 사용 중인 세탁기가 있는지 확인
        List<LaundrySession> runningSessions = sessionRepository.findByMemberAndStatus(member, SessionStatus.RUNNING);
        if (!runningSessions.isEmpty()) {
            throw new BusinessException(ErrorCode.CONFLICT, "이미 사용 중인 세탁기가 있습니다.");
        }

        int durationSeconds = durationMinutes * 60;

        // 세탁기 상태 업데이트 (IN_USE로 변경)
        machine.startUse();

        // 세션 생성 (endAt은 여기에 저장됨)
        LaundrySession session = LaundrySession.builder()
                .laundryMachine(machine)
                .member(member)
                .durationSeconds(durationSeconds)
                .build();
        sessionRepository.save(session);

        log.info("세탁기 사용 시작 완료: sessionId={}, endAt={}", session.getId(), session.getEndAt());

        return session;
    }

    /**
     * 세탁기 사용 종료
     */
    @Transactional
    public void finishLaundry(Long memberId, Long sessionId) {
        log.info("세탁기 사용 종료: memberId={}, sessionId={}", memberId, sessionId);

        LaundrySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "세션을 찾을 수 없습니다."));

        // 본인 확인
        if (!session.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "세션을 종료할 권한이 없습니다.");
        }

        if (session.getStatus() != SessionStatus.RUNNING) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "실행 중인 세션이 아닙니다.");
        }

        // 세션 종료
        session.finish();

        // 세탁기 상태 업데이트
        session.getLaundryMachine().finishUse();

        log.info("세탁기 사용 종료 완료: sessionId={}", sessionId);
    }

    /**
     * 내가 사용 중인 세탁기 목록 조회 (DTO 반환)
     */
    @Transactional(readOnly = true)
    public List<LaundrySessionResponse> getMyRunningSessions(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<LaundrySession> sessions = sessionRepository.findByMemberAndStatusWithMachine(member, SessionStatus.RUNNING);

        // 트랜잭션 내에서 DTO 변환
        return sessions.stream()
                .map(LaundrySessionResponse::from)
                .toList();
    }

    /**
     * AI 서버에서 혼잡도 예측 데이터 가져오기
     */
    @Transactional
    public CongestionForecastResponse getCongestionForecast(LocalDate date, GenderZone genderZone) {
        log.info("혼잡도 예측 조회: date={}, zone={}", date, genderZone);

        try {
            // AI 서버 호출
            String url = aiServerUrl + "/predict";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("date", date.toString());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<CongestionForecastResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            CongestionForecastResponse forecastData = response.getBody();

            if (forecastData != null) {
                // DB에 저장
                saveCongestionForecast(date, genderZone, forecastData);
            }

            return forecastData;

        } catch (Exception e) {
            log.error("AI 서버 호출 실패", e);

            // DB에서 조회 (fallback)
            List<LaundryCongestionForecast> savedForecasts = congestionRepository
                    .findByForecastDateAndGenderZoneOrderByHourAsc(date, genderZone);

            if (!savedForecasts.isEmpty()) {
                return convertToResponse(date, savedForecasts);
            }

            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "혼잡도 예측 데이터를 가져올 수 없습니다.");
        }
    }

    /**
     * 혼잡도 예측 데이터 DB에 저장
     */
    @Transactional
    public void saveCongestionForecast(LocalDate date, GenderZone genderZone, CongestionForecastResponse aiResponse) {
        log.info("혼잡도 예측 데이터 저장: date={}, zone={}", date, genderZone);

        // 기존 데이터 삭제
        congestionRepository.deleteByForecastDateAndGenderZone(date, genderZone);

        // 새 데이터 저장
        for (CongestionForecastResponse.TimelineForecast timeline : aiResponse.getTimeline()) {
            LaundryCongestionForecast forecast = LaundryCongestionForecast.builder()
                    .forecastDate(date)
                    .hour(timeline.getHour())
                    .genderZone(genderZone)
                    .congestion(timeline.getPredictedCongestion())
                    .peakMessage(aiResponse.getPeakMessage())
                    .recommendMessage(aiResponse.getRecommendMessage())
                    .build();
            congestionRepository.save(forecast);
        }

        log.info("혼잡도 예측 데이터 저장 완료");
    }

    /**
     * DB 데이터를 Response로 변환
     */
    private CongestionForecastResponse convertToResponse(LocalDate date, List<LaundryCongestionForecast> forecasts) {
        if (forecasts.isEmpty()) {
            return null;
        }

        LaundryCongestionForecast first = forecasts.getFirst();

        List<CongestionForecastResponse.TimelineForecast> timeline = forecasts.stream()
                .map(f -> CongestionForecastResponse.TimelineForecast.builder()
                        .hour(f.getHour())
                        .predictedCongestion(f.getCongestion())
                        .build())
                .toList();

        return CongestionForecastResponse.builder()
                .date(date.toString())
                .peakMessage(first.getPeakMessage())
                .recommendMessage(first.getRecommendMessage())
                .timeline(timeline)
                .build();
    }
}

