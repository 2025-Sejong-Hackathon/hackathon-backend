package com.hackathon.backend.domain.auth.service;

import com.hackathon.backend.api.auth.dto.LoginRequest;
import com.hackathon.backend.api.auth.dto.LoginResponse;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.service.MemberService;
import com.hackathon.backend.global.exception.BusinessException;
import com.hackathon.backend.global.exception.ErrorCode;
import com.hackathon.backend.global.jwt.JwtTokenProvider;
import com.chuseok22.sejongportallogin.core.SejongMemberInfo;
import com.chuseok22.sejongportallogin.infrastructure.SejongPortalLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SejongPortalLoginService sejongPortalLoginService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            log.info("세종대 포털 로그인 시도: {}", request.getStudentId());

            // 세종대 포털 인증
            SejongMemberInfo sejongMemberInfo = sejongPortalLoginService.getMemberAuthInfos(
                    request.getStudentId(),
                    request.getPassword()
            );

            if (sejongMemberInfo == null) {
                log.error("세종대 포털 로그인 실패: {}", request.getStudentId());
                throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
            }

            log.info("세종대 포털 로그인 성공: {} ({})", sejongMemberInfo.getName(), request.getStudentId());

            // 신규 회원 여부 확인
            boolean isNewMember = memberService.isNewMember(request.getStudentId());

            // 회원 정보 저장 또는 업데이트
            Member member = memberService.createOrUpdateMember(sejongMemberInfo);

            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getStudentId());
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

            log.info("JWT 토큰 발급 완료: memberId={}", member.getId());

            // 응답 생성
            return LoginResponse.builder()
                    .memberId(member.getId())
                    .studentId(member.getStudentId())
                    .name(member.getName())
                    .major(member.getMajor())
                    .grade(member.getGrade())
                    .completedSemesters(member.getCompletedSemester())
                    .status(member.getStatus().name())
                    .isNewMember(isNewMember)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .build();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
