package com.upper6.studentcourse.config.security;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 当前登录用户信息（含 userId、refId），用于 Controller 获取当前学生/教师 ID
 */
@Getter
public class AuthPrincipal implements UserDetails {

    private final String username;
    private final String role;
    private final Long userId;
    private final Long refId;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthPrincipal(String username, String role, Long userId, Long refId, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.role = role;
        this.userId = userId;
        this.refId = refId;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return null;
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
