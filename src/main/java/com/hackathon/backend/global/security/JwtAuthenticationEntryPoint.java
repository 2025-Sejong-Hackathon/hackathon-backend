package com.hackathon.backend.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출
 * 401 Unauthorized 응답 반환
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {

        log.error("인증되지 않은 접근: {} {}", request.getMethod(), request.getRequestURI());
        log.error("인증 실패 원인: {}", authException.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("""
                {
                    "code": "AUTH-001",
                    "message": "인증이 필요합니다. 로그인 후 이용해주세요.",
                    "timestamp": "%s"
                }
                """.formatted(java.time.LocalDateTime.now()));
    }
}

