package com.hackathon.backend.api.auth.controller;

import com.hackathon.backend.api.auth.dto.LoginRequest;
import com.hackathon.backend.api.auth.dto.LoginResponse;
import com.hackathon.backend.domain.auth.service.AuthService;
import com.hackathon.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "세종대 포털 로그인",
            description = "세종대학교 포털 계정으로 로그인합니다. 최초 로그인 시 회원 정보가 자동으로 생성됩니다."
    )
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("로그인 요청: {}", request.getStudentId());
        LoginResponse response = authService.login(request);
        log.info("로그인 성공: {} ({})", response.getName(), response.getStudentId());
        return ApiResponse.success(response);
    }
}
