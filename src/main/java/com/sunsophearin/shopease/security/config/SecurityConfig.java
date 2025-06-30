package com.sunsophearin.shopease.security.config;

import com.sunsophearin.shopease.security.JwtAuthenticationFilter;
import com.sunsophearin.shopease.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.sunsophearin.shopease.security.service.impl.CustomOAuth2UserService;
import com.sunsophearin.shopease.security.service.impl.UserDetailsServiceImpl;
import com.sunsophearin.shopease.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final OAuth2AuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ អនុញ្ញាតឲ្យប្រើបានសេរី (មិនចាំបាច់មាន Token)
                        .requestMatchers(
                                "/api/auth/**",
                                "/oauth2/**",
                                "/api/products/**",
                                "/api/category/**",
                                "/api/categoryType/**",
                                "/api/colors/**",
                                "/api/test/**",
                                "/api/size/**",
                                "/api/auth/**",
                                "/api/payment/**"
                        ).permitAll()

                        // ✅ អនុញ្ញាត GET ក្នុង /api/payment/** (សម្រាប់មើល QR Code ឬព័ត៌មានទូទៅ)
//                        .requestMatchers(HttpMethod.GET, "/api/payment/**").permitAll()

                        // 🔐 ត្រូវការជា User មាន Token បើត្រូវ POST ឬ PUT /api/payment/**
//                        .requestMatchers("/api/payment/**").authenticated()

                        // 🔐 ត្រូវមាន Role ជា Admin
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 🔐 អ្នកប្រើដែលមាន Role USER ឬ ADMIN
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

                        // 🔐 សំណើផ្សេងទៀត ត្រូវមានការផ្ទៀងផ្ទាត់
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
