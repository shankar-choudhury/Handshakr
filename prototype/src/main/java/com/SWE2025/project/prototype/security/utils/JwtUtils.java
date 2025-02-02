package com.SWE2025.project.prototype.security.utils;

public interface JwtUtils {
    String generateToken(String username);
    String getUserNameFromToken(String token);
    boolean validateToken(String token);
}
