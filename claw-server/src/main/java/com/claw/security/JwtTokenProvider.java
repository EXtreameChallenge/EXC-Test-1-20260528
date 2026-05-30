package com.claw.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(@Value(value="${jwt.secret}") String secret, @Value(value="${jwt.access-token-expiration}") long accessTokenExpiration, @Value(value="${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.key = Keys.hmacShaKeyFor((byte[])secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(Long userId, String roleKey) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.accessTokenExpiration);
        return Jwts.builder().subject(String.valueOf(userId)).claim("role", (Object)roleKey).issuedAt(now).expiration(expiryDate).signWith((Key)this.key).compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.refreshTokenExpiration);
        return Jwts.builder().subject(String.valueOf(userId)).claim("type", "refresh").issuedAt(now).expiration(expiryDate).signWith((Key)this.key).compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = this.parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    public String getRoleFromToken(String token) {
        Claims claims = this.parseToken(token);
        return (String)claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            this.parseToken(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        Claims claims = this.parseToken(token);
        return "refresh".equals(claims.get("type", String.class));
    }

    public long getAccessTokenExpiration() {
        return this.accessTokenExpiration;
    }

    private Claims parseToken(String token) {
        return (Claims)Jwts.parser().verifyWith(this.key).build().parseSignedClaims((CharSequence)token).getPayload();
    }
}
