package christmas.christmas_backend.global.security.annotation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 현재 인증된 사용자 정보를 컨트롤러 메서드 파라미터로 주입
 * SecurityContext에서 자동으로 MemberDetails를 가져옴
 *
 * 사용 예시:
 * @GetMapping("/me")
 * public ResponseEntity<MemberResponse> getMyInfo(@CurrentMember MemberDetails memberDetails) {
 *     // memberDetails 사용
 * }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
public @interface CurrentMember {
}

