package com.hackathon.backend.api.member.controller;

import com.hackathon.backend.api.member.dto.MemberResponse;
import com.hackathon.backend.global.security.MemberDetails;
import com.hackathon.backend.global.security.annotation.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "회원", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 정보를 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(@CurrentMember MemberDetails memberDetails) {
        log.info("내 정보 조회: memberId={}", memberDetails.getMemberId());

        MemberResponse response = MemberResponse.from(memberDetails.getMember());
        return ResponseEntity.ok(response);
    }
}

