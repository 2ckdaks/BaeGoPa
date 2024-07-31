package com.portfolio.BaeGoPa.user;

import com.portfolio.BaeGoPa.user.db.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${jwt.secret}")
    private String jwtKey;

    private static SecretKey key;
    private static JwtUtil instance;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
        instance = this;
    }

    public static JwtUtil getInstance() {
        return instance;
    }

    // JWT 만들어주는 함수
    public static String createToken(Authentication authentication) {
        if (instance == null || key == null) {
            throw new IllegalStateException("JwtUtil is not initialized.");
        }

        var user = (CustomUser) authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream()
                .map(it -> it.getAuthority()).collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .claim("username", user.getUsername())
                .claim("displayName", user.getDisplayName())
                .claim("userType", user.getType())
                .claim("authority", authorities) // 권한이 있다면 추가
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(key)
                .compact();
        return jwt;
    }

    // JWT 까주는 함수
    public static Claims extractToken(String token) {
        if (instance == null || key == null) {
            throw new IllegalStateException("JwtUtil is not initialized.");
        }

        Claims claims = Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims;
    }

    public boolean validateToken(String token, User userDetails) {
        final String username = extractToken(token).get("username", String.class);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractToken(token).getExpiration().before(new Date());
    }
}
