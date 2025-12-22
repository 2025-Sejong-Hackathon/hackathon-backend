package christmas.christmas_backend.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 인증 관련 유틸리티 클래스
 * 현재 로그인한 사용자 정보를 쉽게 가져올 수 있는 헬퍼 메서드 제공
 */
@Slf4j
@Component
public class SecurityUtils {

    /**
     * 현재 인증된 사용자의 MemberDetails 가져오기
     */
    public static MemberDetails getCurrentMemberDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("인증 정보가 없습니다.");
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof MemberDetails) {
            return (MemberDetails) principal;
        }

        log.warn("Principal이 MemberDetails 타입이 아닙니다: {}", principal.getClass().getName());
        return null;
    }

    /**
     * 현재 인증된 사용자의 회원 ID 가져오기
     */
    public static Long getCurrentMemberId() {
        MemberDetails memberDetails = getCurrentMemberDetails();
        return memberDetails != null ? memberDetails.getMemberId() : null;
    }

    /**
     * 현재 인증된 사용자의 학번 가져오기
     */
    public static String getCurrentStudentId() {
        MemberDetails memberDetails = getCurrentMemberDetails();
        return memberDetails != null ? memberDetails.getStudentId() : null;
    }

    /**
     * 현재 사용자가 인증되었는지 확인
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
               authentication.isAuthenticated() &&
               authentication.getPrincipal() instanceof MemberDetails;
    }
}

