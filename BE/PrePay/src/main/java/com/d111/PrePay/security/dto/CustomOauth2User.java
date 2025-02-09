package com.d111.PrePay.security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOauth2User implements OAuth2User {

    private final UserDTO userDto;

    public CustomOauth2User(UserDTO userDto) {
        this.userDto = userDto;
    }

    @Override
    public String getName() {
        return userDto.getName();
    }

    public Long getUserId(){
        return userDto.getUserId();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
