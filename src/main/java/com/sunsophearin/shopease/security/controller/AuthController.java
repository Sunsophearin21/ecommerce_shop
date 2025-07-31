package com.sunsophearin.shopease.security.controller;

import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.service.impl.UserServiceImpl;
import com.sunsophearin.shopease.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserServiceImpl userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        try {
            String email = jwtUtil.getEmailFromRequest(request);
            User user = userService.findUserName(email); // load user entity from database

            List<String> roles = authentication.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "username", user.getName(),
                    "picture", user.getPicture(),
                    "roles", roles,
                    "firstPurchase",user.isFirstPurchase()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Create an expired token cookie to clear it
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true) // ➜ set to false in dev if not using HTTPS
                .path("/")
                .maxAge(0) // ➜ Remove immediately
                .sameSite("None") // ➜ Use 'Lax' or 'None' based on frontend domain
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

}
