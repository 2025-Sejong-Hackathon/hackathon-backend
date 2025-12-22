package com.hackathon.backend.global.security;

import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.entity.MemberStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security에서 사용하는 UserDetails 구현체
 * 인증된 사용자 정보를 담는 객체
 */
@Getter
public class MemberDetails implements UserDetails {

    private final Member member;

    public MemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기본적으로 ROLE_USER 권한 부여
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // 세종대 포털 인증을 사용하므로 비밀번호는 없음
        return null;
    }

    @Override
    public String getUsername() {
        return member.getStudentId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // MemberStatus가 ACTIVE인 경우에만 활성
        return member.getStatus() == MemberStatus.ACTIVE;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public String getStudentId() {
        return member.getStudentId();
    }
}

