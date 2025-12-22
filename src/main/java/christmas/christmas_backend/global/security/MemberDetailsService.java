package christmas.christmas_backend.global.security;

import christmas.christmas_backend.domain.member.entity.Member;
import christmas.christmas_backend.domain.member.repository.MemberRepository;
import christmas.christmas_backend.global.exception.BusinessException;
import christmas.christmas_backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Security의 UserDetailsService 구현체
 * JWT 토큰에서 추출한 학번으로 사용자 정보를 조회
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        log.debug("사용자 정보 조회: {}", studentId);

        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없음: {}", studentId);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + studentId);
                });

        return new MemberDetails(member);
    }

    /**
     * 회원 ID로 사용자 정보 조회
     */
    public UserDetails loadUserById(Long memberId) {
        log.debug("사용자 정보 조회 (ID): {}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new MemberDetails(member);
    }
}

