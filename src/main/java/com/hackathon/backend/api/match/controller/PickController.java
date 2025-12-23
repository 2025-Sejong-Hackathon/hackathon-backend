package com.hackathon.backend.api.match.controller;

import com.hackathon.backend.api.match.dto.MatchPairResponse;
import com.hackathon.backend.api.match.dto.PickResponse;
import com.hackathon.backend.domain.match.service.PickService;
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
@Tag(name = "Pick", description = "Pick 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/picks")
public class PickController {

    private final PickService pickService;

    @Operation(
            summary = "회원 Pick",
            description = """
                    다른 회원을 Pick(찜)합니다.
                    
                    - 서로 Pick하면 자동으로 MatchPair가 생성됩니다.
                    - 자기 자신은 Pick할 수 없습니다.
                    - 이미 Pick한 회원은 중복 Pick할 수 없습니다.
                    """
    )
    @PostMapping("/{toMemberId}")
    public ResponseEntity<ApiResponse<PickResponse>> createPick(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long toMemberId) {
        log.info("Pick 생성 요청: fromMemberId={}, toMemberId={}", memberDetails.getMemberId(), toMemberId);

        PickResponse response = pickService.createPick(memberDetails.getMemberId(), toMemberId);

        String message = response.getIsMutual()
                ? "서로 선택하여 매칭 페어가 생성되었습니다!"
                : "선택이 완료되었습니다.";

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    @Operation(
            summary = "Pick 취소",
            description = "이전에 Pick한 회원에 대한 선택을 취소합니다. (매칭이 진행 중이면 취소 불가)"
    )
    @DeleteMapping("/{toMemberId}")
    public ResponseEntity<ApiResponse<Void>> cancelPick(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long toMemberId) {
        log.info("Pick 취소 요청: fromMemberId={}, toMemberId={}", memberDetails.getMemberId(), toMemberId);

        pickService.cancelPick(memberDetails.getMemberId(), toMemberId);

        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(
            summary = "내가 Pick한 목록 조회",
            description = "내가 선택한 회원 목록을 조회합니다."
    )
    @GetMapping("/my-picks")
    public ResponseEntity<ApiResponse<List<PickResponse>>> getMyPicks(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("내가 Pick한 목록 조회: memberId={}", memberDetails.getMemberId());

        List<PickResponse> responses = pickService.getMyPicks(memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(
            summary = "나를 Pick한 목록 조회",
            description = "나를 선택한 회원 목록을 조회합니다."
    )
    @GetMapping("/picks-to-me")
    public ResponseEntity<ApiResponse<List<PickResponse>>> getPicksToMe(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("나를 Pick한 목록 조회: memberId={}", memberDetails.getMemberId());

        List<PickResponse> responses = pickService.getPicksToMe(memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(
            summary = "내 매칭 페어 목록 조회",
            description = "서로 Pick하여 생성된 매칭 페어 목록을 조회합니다."
    )
    @GetMapping("/match-pairs")
    public ResponseEntity<ApiResponse<List<MatchPairResponse>>> getMyMatchPairs(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("매칭 페어 목록 조회: memberId={}", memberDetails.getMemberId());

        List<MatchPairResponse> responses = pickService.getMyMatchPairs(memberDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}

