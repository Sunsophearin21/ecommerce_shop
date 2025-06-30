package com.sunsophearin.shopease.security.service.impl;

import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"+username));
        String password = user.getPassword() != null ? user.getPassword() : "oauth2-user-password";
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                password,
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList())
        );
    }
}
