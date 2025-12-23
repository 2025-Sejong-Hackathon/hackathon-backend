package com.hackathon.backend.api.match.controller;

import com.hackathon.backend.api.match.dto.AiRecommendResponse;
import com.hackathon.backend.domain.match.service.MatchService;
import com.hackathon.backend.global.response.ApiResponse;
import com.hackathon.backend.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "매칭", description = "룸메이트 매칭 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/match")
public class MatchController {

    private final MatchService matchService;

    @Operation(
            summary = "룸메이트 추천 조회",
            description = """
                    AI 서버를 통해 나와 잘 맞는 룸메이트 후보를 추천받습니다.
                    
                    - GIKBTI 설문을 먼저 완료해야 합니다.
                    - 회원 정보(온보딩)가 입력되어 있어야 합니다.
                    - AI 서버에서 매칭률과 일치/불일치 항목을 분석하여 반환합니다.
                    """
    )
    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<AiRecommendResponse>>> getRecommendations(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("룸메이트 추천 조회 요청: memberId={}", memberDetails.getMemberId());

        List<AiRecommendResponse> recommendations = matchService.getRecommendations(memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(
                String.format("총 %d명의 추천 결과를 찾았습니다.", recommendations.size()),
                recommendations));
    }
}

