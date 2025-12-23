package com.hackathon.backend.api.gikbti.controller;

import com.hackathon.backend.api.gikbti.dto.GikbtiQuestionResponse;
import com.hackathon.backend.api.gikbti.dto.GikbtiSubmitRequest;
import com.hackathon.backend.api.gikbti.dto.GikbtiSubmitResponse;
import com.hackathon.backend.domain.gikbti.service.GikbtiService;
import com.hackathon.backend.global.response.ApiResponse;
import com.hackathon.backend.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "GIKBTI", description = "기숙사 MBTI 설문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gikbti")
public class GikbtiController {

    private final GikbtiService gikbtiService;

    @Operation(
            summary = "GIKBTI 질문 목록 조회",
            description = "기숙사 MBTI 설문의 모든 질문과 옵션을 조회합니다. 활성화된 질문만 표시됩니다."
    )
    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<GikbtiQuestionResponse>>> getQuestions() {
        log.info("GIKBTI 질문 목록 조회 요청");

        List<GikbtiQuestionResponse> questions = gikbtiService.getAllQuestions();
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    @Operation(
            summary = "GIKBTI 응답 제출",
            description = """
                    GIKBTI 설문의 모든 응답을 제출하고 타입을 계산합니다.
                    
                    - 16개 질문에 대한 응답을 모두 제출해야 합니다.
                    - 각 질문당 하나의 옵션만 선택 가능합니다.
                    - 이전 응답이 있으면 덮어씌워집니다.
                    - 계산된 GIKBTI 타입은 회원 정보에 자동으로 저장됩니다.
                    """
    )
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<GikbtiSubmitResponse>> submitAnswers(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Valid @RequestBody GikbtiSubmitRequest request) {
        log.info("GIKBTI 응답 제출: memberId={}", memberDetails.getMemberId());

        String gikbtiType = gikbtiService.submitAnswers(memberDetails.getMemberId(), request.getAnswers());

        GikbtiSubmitResponse response = GikbtiSubmitResponse.of(memberDetails.getMemberId(), gikbtiType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}


