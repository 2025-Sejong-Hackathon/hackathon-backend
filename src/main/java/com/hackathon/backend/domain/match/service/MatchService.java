package com.hackathon.backend.domain.match.service;

import com.hackathon.backend.api.match.dto.AiRecommendRequest;
import com.hackathon.backend.api.match.dto.AiRecommendResponse;
import com.hackathon.backend.domain.gikbti.entity.UserGikbtiAnswer;
import com.hackathon.backend.domain.gikbti.repository.UserGikbtiAnswerRepository;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.entity.UserPreferences;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.hackathon.backend.domain.member.repository.UserPreferencesRepository;
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
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final UserGikbtiAnswerRepository gikbtiAnswerRepository;
    private final UserPreferencesRepository userPreferencesRepository;

    @Value("${ai.server.url:http://43.203.41.246:8002}")
    private String aiServerUrl;

    /**
     * AI 서버에 룸메이트 추천 요청
     */
    public List<AiRecommendResponse> getRecommendations(Long memberId) {
        log.info("룸메이트 추천 요청: memberId={}", memberId);

        // 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // GIKBTI 응답 조회
        List<UserGikbtiAnswer> answers = gikbtiAnswerRepository.findByMemberId(memberId);
        if (answers.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "GIKBTI 설문을 먼저 완료해주세요.");
        }

        // AI 요청 DTO 생성
        AiRecommendRequest request = buildAiRequest(member, answers);

        // AI 서버 호출
        try {
            String url = aiServerUrl + "/recommend";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AiRecommendRequest> entity = new HttpEntity<>(request, headers);

            log.info("AI 서버 요청: url={}, request={}", url, request);

            ResponseEntity<List<AiRecommendResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<AiRecommendResponse>>() {}
            );

            List<AiRecommendResponse> recommendations = response.getBody();
            log.info("AI 서버 응답: count={}", recommendations != null ? recommendations.size() : 0);

            return recommendations;

        } catch (Exception e) {
            log.error("AI 서버 호출 실패", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "추천 시스템 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * AI 요청 DTO 생성
     */
    private AiRecommendRequest buildAiRequest(Member member, List<UserGikbtiAnswer> answers) {
        // 나이 계산
        Integer age = member.getBirthDate() != null
                ? Period.between(member.getBirthDate(), LocalDate.now()).getYears()
                : null;

        // UserPreferences 조회 (룸메이트 선호도 - 별도 테이블)
        UserPreferences preferences = userPreferencesRepository.findByMember(member).orElse(null);
        Boolean wantsSmoker = preferences != null ? preferences.getRoommateSmokingPref() : null;
        Boolean wantsDrinker = preferences != null ? preferences.getRoommateDrinkingPref() : null;

        // GIKBTI 응답을 Map으로 변환
        Map<Long, Integer> answerMap = new HashMap<>();
        for (UserGikbtiAnswer answer : answers) {
            Long questionId = answer.getGikbtiQuestion().getId();
            Integer value = answer.getGikbtiOption().getIsTrue() ? 1 : 0;
            answerMap.put(questionId, value);
        }

        return AiRecommendRequest.builder()
                .studentId(member.getStudentId())
                .age(age)
                .gender(member.getGender() != null ? member.getGender().name() : null)
                .major(member.getMajor())
                .isSmoker(member.getIsSmoker())
                .wantsSmoker(wantsSmoker != null ? wantsSmoker : false) // UserPreferences에서 조회
                .isDrinker(member.getIsDrinker())
                .wantsDrinker(wantsDrinker != null ? wantsDrinker : false) // UserPreferences에서 조회
                .sensitiveHeat(member.getHeatSensitive())
                .sensitiveCold(member.getColdSensitive())
                // GIKBTI 응답 매핑 (질문 ID 1-16)
                .sleepHabit(answerMap.getOrDefault(1L, 0))
                .wakeUp(answerMap.getOrDefault(2L, 0))
                .activityTime(answerMap.getOrDefault(3L, 0))
                .outReturn(answerMap.getOrDefault(4L, 0))
                .cleanImmediate(answerMap.getOrDefault(5L, 0))
                .deskStatus(answerMap.getOrDefault(6L, 0))
                .cleanCycle(answerMap.getOrDefault(7L, 0))
                .otherSeatTol(answerMap.getOrDefault(8L, 0))
                .phoneNoise(answerMap.getOrDefault(9L, 0))
                .lightSensitivity(answerMap.getOrDefault(10L, 0))
                .keyMouseNoise(answerMap.getOrDefault(11L, 0))
                .alarmHabit(answerMap.getOrDefault(12L, 0))
                .socialWillingness(answerMap.getOrDefault(13L, 0))
                .friendInvite(answerMap.getOrDefault(14L, 0))
                .dormStay(answerMap.getOrDefault(15L, 0))
                .spacePrivacy(answerMap.getOrDefault(16L, 0))
                .build();
    }
}
