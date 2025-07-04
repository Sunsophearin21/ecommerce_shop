package com.sunsophearin.shopease.security.oauth2;

import com.sunsophearin.shopease.security.entities.Role;
import com.sunsophearin.shopease.security.entities.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class UserPrincipal implements OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;

    public UserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return user.getUsername(); // or getEmail()
    }
}
