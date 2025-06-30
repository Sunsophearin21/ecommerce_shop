package com.sunsophearin.shopease.security.controller;

import com.sunsophearin.shopease.security.dto.LoginRequest;
import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.repository.UserRepository;
import com.sunsophearin.shopease.security.service.impl.UserServiceImpl;
import com.sunsophearin.shopease.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserServiceImpl userService;
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromCookie(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String email = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);
            User user = userService.findUserName(email);

            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "roles", roles,
                    "username", user.getId(),// Add username field
                    "picture",user.getPicture()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
//@GetMapping("/profile")
//public ResponseEntity<User> getProfile(@AuthenticationPrincipal OAuth2User principal) {
//    // Get email or username from OAuth2 principal
//    String email = principal.getAttribute("email"); // Or "preferred_username" for some providers
//    // Fetch user from your database
//    User user = userService.findUserName(email);
//    return ResponseEntity.ok(user);
//}
//    @GetMapping("/profile")
//    public ResponseEntity<?> getProfile(Authentication authentication) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            User userName = userService.findUserName(authentication.getName());
//            return ResponseEntity.ok(userName);
//            // Extract claims as needed
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
@GetMapping("/me")
public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
    // ពិនិត្យ null និងស្ថានភាពផ្ទៀងផ្ទាត់
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // យកអ៊ីមែលពី principal (ដំណើរការសម្រាប់ JWT និង OAuth2)
    String email = authentication.getName();

    // យកព័ត៌មានអ្នកប្រើពី database
    User user = userService.findUserName(email);

    // យក roles ពី authorities
    List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    // បង្កើត response
    return ResponseEntity.ok(Map.of(
            "email", email,
            "roles", roles,
            "username", user.getName(),
            "picture", user.getPicture()
    ));
}


}
