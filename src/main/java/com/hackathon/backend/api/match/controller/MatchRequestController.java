package com.hackathon.backend.api.match.controller;

import com.hackathon.backend.api.match.dto.MatchRequestResponse;
import com.hackathon.backend.api.match.dto.RoommateMatchResponse;
import com.hackathon.backend.domain.match.service.MatchRequestService;
import com.hackathon.backend.global.response.ApiResponse;
import com.hackathon.backend.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "매칭 요청", description = "매칭 요청 및 확정 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/match-requests")
public class MatchRequestController {

    private final MatchRequestService matchRequestService;

    @Operation(
            summary = "매칭 요청 생성",
            description = """
                    매칭 페어에 대해 룸메이트 매칭 확정 요청을 보냅니다.
                    
                    - 채팅방에서 상대방에게 최종 매칭 요청을 보낼 때 사용합니다.
                    - MatchPair의 회원만 요청을 보낼 수 있습니다.
                    - 이미 대기 중인 요청이 있으면 중복 요청 불가합니다.
                    """
    )
    @PostMapping("/match-pairs/{matchPairId}")
    public ResponseEntity<ApiResponse<MatchRequestResponse>> createMatchRequest(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long matchPairId) {
        log.info("매칭 요청 생성: senderId={}, matchPairId={}", memberDetails.getMemberId(), matchPairId);

        MatchRequestResponse response = matchRequestService.createMatchRequest(
                memberDetails.getMemberId(), matchPairId);

        return ResponseEntity.ok(ApiResponse.success("매칭 요청을 보냈습니다.", response));
    }

    @Operation(
            summary = "매칭 요청 수락",
            description = """
                    상대방의 매칭 요청을 수락하여 룸메이트 매칭을 확정합니다.
                    
                    - 수락 시 RoommateMatch가 생성되어 최종 매칭이 완료됩니다.
                    - 요청 수신자만 수락할 수 있습니다.
                    """
    )
    @PostMapping("/{matchRequestId}/accept")
    public ResponseEntity<ApiResponse<RoommateMatchResponse>> acceptMatchRequest(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long matchRequestId) {
        log.info("매칭 요청 수락: receiverId={}, matchRequestId={}",
                memberDetails.getMemberId(), matchRequestId);

        RoommateMatchResponse response = matchRequestService.acceptMatchRequest(
                memberDetails.getMemberId(), matchRequestId);

        return ResponseEntity.ok(ApiResponse.success("룸메이트 매칭이 확정되었습니다!", response));
    }

    @Operation(
            summary = "매칭 요청 거절",
            description = "상대방의 매칭 요청을 거절합니다."
    )
    @PostMapping("/{matchRequestId}/reject")
    public ApiResponse<Void> rejectMatchRequest(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long matchRequestId) {
        log.info("매칭 요청 거절: receiverId={}, matchRequestId={}",
                memberDetails.getMemberId(), matchRequestId);

        matchRequestService.rejectMatchRequest(memberDetails.getMemberId(), matchRequestId);

        return ApiResponse.success();
    }

    @Operation(
            summary = "받은 매칭 요청 목록 조회",
            description = "다른 회원이 나에게 보낸 매칭 요청 목록을 조회합니다. (대기 중인 요청만)"
    )
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<List<MatchRequestResponse>>> getReceivedMatchRequests(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("받은 매칭 요청 목록 조회: memberId={}", memberDetails.getMemberId());

        List<MatchRequestResponse> responses = matchRequestService.getReceivedMatchRequests(
                memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(
            summary = "내 룸메이트 매칭 조회",
            description = "확정된 룸메이트 매칭 정보를 조회합니다."
    )
    @GetMapping("/roommate-matches")
    public ResponseEntity<ApiResponse<List<RoommateMatchResponse>>> getMyRoommateMatches(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("룸메이트 매칭 조회: memberId={}", memberDetails.getMemberId());

        List<RoommateMatchResponse> responses = matchRequestService.getMyRoommateMatches(
                memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}

