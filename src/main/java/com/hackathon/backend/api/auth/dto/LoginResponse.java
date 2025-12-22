package com.hackathon.backend.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "로그인 응답")
@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    @Schema(description = "회원 ID")
    private Long memberId;

    @Schema(description = "학번")
    private String studentId;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "학과")
    private String major;

    @Schema(description = "학년")
    private String grade;

    @Schema(description = "이수 학기")
    private String completedSemesters;

    @Schema(description = "사용자 상태")
    private String status;

    @Schema(description = "신규 회원 여부")
    private Boolean isNewMember;

    @Schema(description = "Access Token")
    private String accessToken;

    @Schema(description = "Refresh Token")
    private String refreshToken;

    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType;
}

