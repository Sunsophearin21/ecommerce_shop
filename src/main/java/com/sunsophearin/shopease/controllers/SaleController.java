package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.PaymentRequest;
import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.dto.SaleRequestDTO;
import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.repository.UserRepository;
import com.sunsophearin.shopease.security.service.impl.UserServiceImpl;
import com.sunsophearin.shopease.security.utils.JwtUtil;
import com.sunsophearin.shopease.services.PaymentService;
import com.sunsophearin.shopease.services.SaleService;
import com.sunsophearin.shopease.services.impl.PaymentServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
@Slf4j
public class SaleController {
    private final String jwtSecret = "YourSuperSecretKeyAtLeast32CharactersLong123";
    private final SaleService saleService;
    private final PaymentServiceImpl paymentService;
    private final UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/user")
    public ResponseEntity<?> getUser(HttpServletRequest request){
//        jwtUtil.extractAllClaims(request.getCookies());
        String token = extractTokenFromCookie(request );

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String email = claims.getSubject();
            User userName = userService.findUserName(email);
            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "roles", userName
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }

    }
    @GetMapping("/me")
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        String token = extractTokenFromCookie(request );

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String email = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);

            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "roles", roles
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createSale(@RequestBody SaleDto paymentRequest, HttpServletRequest request) {
        String token = extractTokenFromCookie(request );
        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String email = claims.getSubject();
            log.info("Authenticated user: {}", email);
//            Map<String, Object> response = saleService.processSale(dto,email);
            Map<String, Object> stringObjectMap = paymentService.generateKhqr(paymentRequest, email);
            return ResponseEntity.ok(stringObjectMap);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/check-payment")
    public ResponseEntity<Map<String, Object>> checkPayment(@RequestBody Map<String, String> payload) {
        String md5 = payload.get("md5");
        if (md5 == null || md5.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "MD5 is required"));
        }

        Map<String, Object> response = paymentService.checkPayment(md5);
        return ResponseEntity.ok(response);
    }
}