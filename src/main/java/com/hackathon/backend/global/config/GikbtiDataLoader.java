package com.hackathon.backend.global.config;

import com.hackathon.backend.domain.gikbti.entity.GikbtiCategory;
import com.hackathon.backend.domain.gikbti.entity.GikbtiOption;
import com.hackathon.backend.domain.gikbti.entity.GikbtiQuestion;
import com.hackathon.backend.domain.gikbti.repository.GikbtiOptionRepository;
import com.hackathon.backend.domain.gikbti.repository.GikbtiQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * GIKBTI 초기 데이터 로더
 * 애플리케이션 시작 시 GIKBTI 질문과 옵션 데이터를 자동으로 삽입합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GikbtiDataLoader implements ApplicationRunner {

    private final GikbtiQuestionRepository questionRepository;
    private final GikbtiOptionRepository optionRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // 이미 데이터가 있으면 건너뛰기
        if (questionRepository.count() > 0) {
            log.info("GIKBTI 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        log.info("GIKBTI 초기 데이터 삽입 시작...");

        List<GikbtiQuestion> questions = createQuestions();
        questionRepository.saveAll(questions);

        List<GikbtiOption> options = createOptions(questions);
        optionRepository.saveAll(options);

        log.info("GIKBTI 초기 데이터 삽입 완료: 질문 {}개, 옵션 {}개", questions.size(), options.size());
    }

    private List<GikbtiQuestion> createQuestions() {
        List<GikbtiQuestion> questions = new ArrayList<>();

        // 아침형/저녁형 (MORNING_EVENING) - 총 10점
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.MORNING_EVENING)
                .text("수면 시간은?")
                .isActive(true)
                .displayOrder(1)
                .weight(3)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.MORNING_EVENING)
                .text("기상 시간은?")
                .isActive(true)
                .displayOrder(2)
                .weight(3)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.MORNING_EVENING)
                .text("주 활동 시간은?")
                .isActive(true)
                .displayOrder(3)
                .weight(2)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.MORNING_EVENING)
                .text("룸메의 외출/복귀 시간이?")
                .isActive(true)
                .displayOrder(4)
                .weight(2)
                .build());

        // 깔끔형/자유형 (CLEAN_DIRTY) - 총 10점
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.CLEAN_DIRTY)
                .text("바닥에 머리카락이 보이면?")
                .isActive(true)
                .displayOrder(1)
                .weight(2)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.CLEAN_DIRTY)
                .text("책상 상태는 보통?")
                .isActive(true)
                .displayOrder(2)
                .weight(3)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.CLEAN_DIRTY)
                .text("청소 주기는?")
                .isActive(true)
                .displayOrder(3)
                .weight(3)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.CLEAN_DIRTY)
                .text("상대방 자리가 더럽다면?")
                .isActive(true)
                .displayOrder(4)
                .weight(2)
                .build());

        // 예민형/둔감형 (SENSITIVE_DULL) - 총 10점
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.SENSITIVE_DULL)
                .text("룸메 전화 통화 소리")
                .isActive(true)
                .displayOrder(1)
                .weight(3)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.SENSITIVE_DULL)
                .text("불 켜진 상태에서 잠들 수 있나")
                .isActive(true)
                .displayOrder(2)
                .weight(3)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.SENSITIVE_DULL)
                .text("키보드/마우스 소리")
                .isActive(true)
                .displayOrder(3)
                .weight(2)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.SENSITIVE_DULL)
                .text("아침 알람 소리")
                .isActive(true)
                .displayOrder(4)
                .weight(2)
                .build());

        // 외향형/내향형 (EXTRO_INTRO) - 총 10점
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.EXTRO_INTRO)
                .text("룸메와의 관계")
                .isActive(true)
                .displayOrder(1)
                .weight(3)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.EXTRO_INTRO)
                .text("룸메가 친구를 데려온다면?")
                .isActive(true)
                .displayOrder(2)
                .weight(2)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.EXTRO_INTRO)
                .text("기숙사에서 보내는 시간은?")
                .isActive(true)
                .displayOrder(3)
                .weight(3)
                .build());
        questions.add(GikbtiQuestion.builder()
                .category(GikbtiCategory.EXTRO_INTRO)
                .text("룸메가 나의 생활공간을 침범한다면?")
                .isActive(true)
                .displayOrder(4)
                .weight(2)
                .build());

        return questions;
    }

    private List<GikbtiOption> createOptions(List<GikbtiQuestion> questions) {
        List<GikbtiOption> options = new ArrayList<>();

        // 각 질문의 옵션 데이터 (순서대로)
        String[][] optionTexts = {
                // 아침형/저녁형
                {"12시 이전에 자요", "새벽 2-3시에 자요"},
                {"8시 쯤 일어나서 하루를 시작해요", "나에게 오전은 없다"},
                {"낮시간에 일어나서 작업을 해요", "밤에 작업을 많이 해요"},
                {"이르거나 늦어도 괜찮다", "잘 때 그러면 힘들다"},
                // 깔끔형/자유형
                {"바로 치운다", "한번에 몰아서 치운다"},
                {"항상 정리된 편", "더러운편이다"},
                {"1일 / 3일", "1주 / 1개월"},
                {"참을 수 없다", "상관없다"},
                // 예민형/둔감형
                {"나가서 했으면 좋겠다", "상관없다"},
                {"힘들다", "가능하다"},
                {"거슬린다", "신경 안쓴다"},
                {"바로 끈다", "잘 못들어서 여러번 울린다"},
                // 외향형/내향형
                {"밥도 먹고 친해지고 싶다", "거리를 두고 싶다"},
                {"미리 말하면 괜찮다", "싫다"},
                {"잘때만 들어간다", "수업시간 빼고 많이 들어간다"},
                {"절대 안됨!! 내 공간 지켜", "뭐 어때"}
        };

        for (int i = 0; i < questions.size(); i++) {
            GikbtiQuestion question = questions.get(i);
            String[] texts = optionTexts[i];

            // 첫 번째 옵션 (isTrue = true)
            options.add(GikbtiOption.builder()
                    .gikbtiQuestion(question)
                    .text(texts[0])
                    .isTrue(true)
                    .displayOrder(1)
                    .build());

            // 두 번째 옵션 (isTrue = false)
            options.add(GikbtiOption.builder()
                    .gikbtiQuestion(question)
                    .text(texts[1])
                    .isTrue(false)
                    .displayOrder(2)
                    .build());
        }

        return options;
    }
}

