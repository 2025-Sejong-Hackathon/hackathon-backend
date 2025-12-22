package com.hackathon.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // JWT 보안 스키마 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("JWT 토큰을 입력하세요. (Bearer 접두사 제외)");

        return new OpenAPI()
                .info(new Info()
                        .title("Christmas Backend API")
                        .description("""
                                Christmas 프로젝트 백엔드 API 문서
                                
                                ## 인증 방법
                                1. `/api/v1/auth/login` 엔드포인트로 로그인
                                2. 응답에서 `accessToken` 획득
                                3. 우측 상단 🔒 Authorize 버튼 클릭
                                4. accessToken 값 입력 (Bearer 제외)
                                5. 인증이 필요한 API 호출 가능
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Christmas Team")
                                .email("support@christmas.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("로컬 개발 서버"),
                        new Server().url("https://api.christmas.com").description("프로덕션 서버")
                ))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", securityScheme));
    }
}

