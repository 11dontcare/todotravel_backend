package org.example.todotravel.global.security;

import lombok.Getter;
import org.example.todotravel.domain.user.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 인증 및 권한 관리를 위한 사용자 세부 정보 클래스
 */
public class CustomUserDetails implements UserDetails {

    @Getter
    private final String email;     // 사용자 이메일
    private final String username;  // 사용자 이름
    private final String password;  // 사용자 비밀번호
    private final List<GrantedAuthority> authorities;   // 권한 목록

    /**
     * 생성자
     *
     * @param email    이메일
     * @param username 이름
     * @param password 비밀번호
     * @param role    권한 목록
     */
    public CustomUserDetails(String email, String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}
