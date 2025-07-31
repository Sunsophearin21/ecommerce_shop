package com.sunsophearin.shopease.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key key;
    private final long jwtExpirationInMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.jwtExpiration}") long jwtExpirationInMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    // Simplified token extraction
    public String extractTokenFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    // Consolidated email extraction with proper error handling
    public String getEmailFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            throw new InsufficientAuthenticationException("Missing or invalid JWT token");
        }
        return extractUsername(token);
    }


    // Improved token generation with roles
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", getRoles(userDetails));
        return buildToken(claims, userDetails.getUsername());
    }

    private List<String> getRoles(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }


    private String buildToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Consolidated claims extraction with better error handling
    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final Claims claims = parseToken(token);
            final String username = claims.getSubject();
            final Date expiration = claims.getExpiration();

            return username.equals(userDetails.getUsername()) && !expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public List<String> extractRoles(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            return Collections.emptyList();
        }
        Claims claims = parseToken(token);
        Object roles = claims.get("roles");

        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
