package com.hackathon.backend.api.member.controller;

import com.hackathon.backend.api.member.dto.MemberOnboardingRequest;
import com.hackathon.backend.api.member.dto.MemberResponse;
import com.hackathon.backend.api.member.dto.MemberUpdateRequest;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.service.MemberService;
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

@Slf4j
@Tag(name = "회원", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyInfo(
            @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("내 정보 조회: memberId={}", memberDetails.getMemberId());

        MemberResponse response = MemberResponse.from(memberDetails.getMember());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "회원 온보딩 정보 입력",
            description = "회원가입 후 추가 정보(성별, 생년월일, 흡연/음주 여부, 추위/더위 민감도)를 입력합니다."
    )
    @PutMapping("/onboarding")
    public ResponseEntity<ApiResponse<MemberResponse>> updateOnboardingInfo(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @Valid @RequestBody MemberOnboardingRequest request) {
        log.info("회원 온보딩 정보 입력: memberId={}", memberDetails.getMemberId());

        Member updatedMember = memberService.updateOnboardingInfo(
                memberDetails.getMemberId(),
                request.getGender(),
                request.getBirthDate(),
                request.getIsSmoker(),
                request.getIsDrinker(),
                request.getIsColdSensitive(),
                request.getIsHeatSensitive()
        );

        MemberResponse response = MemberResponse.from(updatedMember);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "회원의 전체 정보를 수정합니다."
    )
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMemberInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Valid @RequestBody MemberUpdateRequest request) {
        log.info("회원 정보 수정: memberId={}", memberDetails.getMemberId());

        Member updatedMember = memberService.updateMemberInfo(
                memberDetails.getMemberId(),
                request.getMajor(),
                request.getGrade(),
                request.getCompletedSemester() != null ? request.getCompletedSemester().toString() : null,
                request.getGender(),
                request.getBirthDate(),
                request.getIsSmoker(),
                request.getIsDrinker(),
                request.getIsColdSensitive(),
                request.getIsHeatSensitive()
        );

        MemberResponse response = MemberResponse.from(updatedMember);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

