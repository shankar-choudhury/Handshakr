package com.SWE2025.project.prototype.security.utils;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtilsImpl implements JwtUtils{
    private final String jwtSecretKey = generateBase64Key();
    private final int jwtExpirationMs = 86400000;
    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(key)
                    .compact();
    }

    @Override
    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException e) {
            throw new RuntimeException(String.format("Invalid JWT signature: %s", e.getMessage()), e.getCause());
        } catch (MalformedJwtException e) {
            throw new RuntimeException(String.format("Invalid JWT token: %s", e.getMessage()), e.getCause());
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(String.format("JWT token is expired: %s", e.getMessage()), e.getCause());
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(String.format("JWT token is unsupported: %s", e.getMessage()), e.getCause());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(String.format("JWT claims string is empty: %s", e.getMessage()), e.getCause());
        }
    }

    private String generateBase64Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException na) {
            throw new RuntimeException("Error using key generation algo: algo does not exits", na);
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT secret key", e);
        }
    }
}
