package com.hackathon.backend.api.gikbti.controller;

import com.hackathon.backend.api.gikbti.dto.GikbtiQuestionResponse;
import com.hackathon.backend.api.gikbti.dto.GikbtiRecalculateResponse;
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
import java.util.Map;

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

    @Operation(
            summary = "전체 사용자 GIKBTI 재계산 (Admin)",
            description = """
                    저장된 UserGikbtiAnswer 데이터를 기반으로 모든 회원의 GIKBTI 타입을 재계산합니다.
                    
                    - 1000명 이상의 사용자 데이터가 있는 경우 사용
                    - 16개 응답이 모두 있는 회원만 처리
                    - 가중치를 적용하여 재계산
                    - 처리 시간이 오래 걸릴 수 있습니다
                    """
    )
    @PostMapping("/recalculate-all")
    public ResponseEntity<ApiResponse<GikbtiRecalculateResponse>> recalculateAll() {
        log.info("전체 사용자 GIKBTI 재계산 요청");

        Map<String, Integer> result = gikbtiService.recalculateAllGikbti();

        GikbtiRecalculateResponse response = GikbtiRecalculateResponse.builder()
                .totalMembers(result.get("total"))
                .successCount(result.get("success"))
                .failCount(result.get("fail"))
                .processingTime(result.get("processingTime").longValue())
                .build();

        return ResponseEntity.ok(ApiResponse.success(
                String.format("총 %d명 중 %d명 재계산 완료 (실패: %d명)",
                        response.getTotalMembers(), response.getSuccessCount(), response.getFailCount()),
                response));
    }

    @Operation(
            summary = "내 GIKBTI 재계산",
            description = """
                    저장된 응답을 기반으로 내 GIKBTI 타입을 재계산합니다.
                    
                    - 16개 응답이 모두 있어야 합니다
                    - 가중치를 적용하여 재계산
                    """
    )
    @PostMapping("/recalculate")
    public ResponseEntity<ApiResponse<GikbtiSubmitResponse>> recalculateMyGikbti(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("내 GIKBTI 재계산: memberId={}", memberDetails.getMemberId());

        String gikbtiType = gikbtiService.recalculateMemberGikbti(memberDetails.getMemberId());

        GikbtiSubmitResponse response = GikbtiSubmitResponse.of(memberDetails.getMemberId(), gikbtiType);
        return ResponseEntity.ok(ApiResponse.success("GIKBTI 타입이 재계산되었습니다.", response));
    }
}


