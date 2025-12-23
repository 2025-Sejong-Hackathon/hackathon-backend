package com.hackathon.backend.domain.gikbti.service;

import com.hackathon.backend.api.gikbti.dto.GikbtiAnswerDto;
import com.hackathon.backend.api.gikbti.dto.GikbtiOptionResponse;
import com.hackathon.backend.api.gikbti.dto.GikbtiQuestionResponse;
import com.hackathon.backend.domain.gikbti.entity.*;
import com.hackathon.backend.domain.gikbti.repository.GikbtiOptionRepository;
import com.hackathon.backend.domain.gikbti.repository.GikbtiQuestionRepository;
import com.hackathon.backend.domain.gikbti.repository.UserGikbtiAnswerRepository;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.hackathon.backend.global.exception.BusinessException;
import com.hackathon.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GikbtiService {

    private final GikbtiQuestionRepository questionRepository;
    private final GikbtiOptionRepository optionRepository;
    private final UserGikbtiAnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    /**
     * 모든 GIKBTI 질문과 옵션 조회 (활성화된 것만)
     * 카테고리 순서: 아침형/저녁형 → 청결형/더러운형 → 예민형/둔한형 → 외향형/내향형
     */
    public List<GikbtiQuestionResponse> getAllQuestions() {
        log.info("GIKBTI 질문 목록 조회");

        List<GikbtiQuestion> questions = questionRepository.findByIsActiveTrueOrderByDisplayOrderAsc();

        return questions.stream()
                .sorted(Comparator.comparing(GikbtiQuestion::getCategory, this::compareCategoryOrder)
                        .thenComparing(GikbtiQuestion::getDisplayOrder))
                .map(question -> {
                    List<GikbtiOption> options = optionRepository.findByGikbtiQuestionIdOrderByDisplayOrderAsc(question.getId());
                    List<GikbtiOptionResponse> optionResponses = options.stream()
                            .map(GikbtiOptionResponse::from)
                            .collect(Collectors.toList());
                    return GikbtiQuestionResponse.from(question, optionResponses);
                })
                .collect(Collectors.toList());
    }

    /**
     * GIKBTI 응답 제출 및 타입 계산
     */
    @Transactional
    public String submitAnswers(Long memberId, List<GikbtiAnswerDto> answers) {
        log.info("GIKBTI 응답 제출: memberId={}, answerCount={}", memberId, answers.size());

        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 기존 응답 삭제 (재응답 허용)
        answerRepository.deleteByMemberId(memberId);

        // 응답 저장
        Map<GikbtiCategory, Integer> categoryScores = new HashMap<>();

        for (GikbtiAnswerDto answer : answers) {
            GikbtiQuestion question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "질문을 찾을 수 없습니다."));

            GikbtiOption option = optionRepository.findById(answer.getOptionId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "옵션을 찾을 수 없습니다."));

            // 응답 저장
            UserGikbtiAnswer userAnswer = UserGikbtiAnswer.builder()
                    .member(member)
                    .gikbtiQuestion(question)
                    .gikbtiOption(option)
                    .build();
            answerRepository.save(userAnswer);

            // 카테고리별 점수 계산 (isTrue가 true면 1점 추가)
            if (option.getIsTrue()) {
                categoryScores.merge(question.getCategory(), 1, Integer::sum);
            }
        }

        // GIKBTI 타입 계산
        String gikbtiType = calculateGikbtiType(categoryScores);

        // Member에 GIKBTI 타입 저장
        member.updateGikbti(gikbtiType);

        log.info("GIKBTI 타입 계산 완료: memberId={}, gikbtiType={}", memberId, gikbtiType);

        return gikbtiType;
    }

    /**
     * 카테고리별 점수를 기반으로 GIKBTI 타입 계산
     * 가중치 합산 점수를 기반으로 판단 (기본 기준: 6점 이상이면 앞 글자, 아니면 뒤 글자)
     * 각 카테고리의 총 가중치는 10점으로 설정 권장
     */
    private String calculateGikbtiType(Map<GikbtiCategory, Integer> categoryScores) {
        StringBuilder gikbtiType = new StringBuilder();

        // MORNING_EVENING: M(아침형) vs E(저녁형)
        int morningScore = categoryScores.getOrDefault(GikbtiCategory.MORNING_EVENING, 0);
        gikbtiType.append(morningScore >= 6 ? "M" : "N");

        // CLEAN_DIRTY: C(청결형) vs D(자유형)
        int cleanScore = categoryScores.getOrDefault(GikbtiCategory.CLEAN_DIRTY, 0);
        gikbtiType.append(cleanScore >= 6 ? "C" : "D");

        // SENSITIVE_DULL: S(예민형) vs D(둔감형)
        int sensitiveScore = categoryScores.getOrDefault(GikbtiCategory.SENSITIVE_DULL, 0);
        gikbtiType.append(sensitiveScore >= 6 ? "S" : "T");

        // EXTRO_INTRO: E(외향형) vs I(내향형)
        int extroScore = categoryScores.getOrDefault(GikbtiCategory.EXTRO_INTRO, 0);
        gikbtiType.append(extroScore >= 6 ? "E" : "I");

        return gikbtiType.toString();
    }

    /**
     * 카테고리 정렬 순서 정의
     * 1. MORNING_EVENING (아침형/저녁형)
     * 2. CLEAN_DIRTY (청결형/더러운형)
     * 3. SENSITIVE_DULL (예민형/둔한형)
     * 4. EXTRO_INTRO (외향형/내향형)
     */
    private int compareCategoryOrder(GikbtiCategory c1, GikbtiCategory c2) {
        return Integer.compare(getCategoryOrder(c1), getCategoryOrder(c2));
    }

    private int getCategoryOrder(GikbtiCategory category) {
        return switch (category) {
            case MORNING_EVENING -> 1;
            case CLEAN_DIRTY -> 2;
            case SENSITIVE_DULL -> 3;
            case EXTRO_INTRO -> 4;
        };
    }
}

