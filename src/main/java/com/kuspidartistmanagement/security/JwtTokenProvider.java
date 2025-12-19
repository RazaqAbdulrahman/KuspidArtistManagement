/*
package com.kuspidartistmanagement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * JWT token provider for authentication and tenant context.
 * Generates and validates JWT tokens containing user and tenant information.
 * Thread-safe and stateless - no business logic, only token operations.

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Environment environment;

    @Value("${app.jwt.secret:}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:3600000}") // default 1 hour
    private long jwtExpiration;

    private SecretKey secretKey;

    private static final String TENANT_ID_CLAIM = "tenantId";
    private static final String USER_ID_CLAIM = "userId";

    /**
     * Initialize the secret key:
     * - In dev: generate a secure random key
     * - In prod: use the configured secret

    @PostConstruct
    public void init() {
        if (isDevProfile()) {
            // Dev: generate random key
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            log.info("Generated random JWT key for DEV environment");
        } else {
            // Prod: use configured secret
            if (jwtSecret == null || jwtSecret.isEmpty()) {
                throw new IllegalStateException("JWT secret must be set for PROD environment");
            }
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    private boolean isDevProfile() {
        String[] profiles = environment.getActiveProfiles();
        for (String profile : profiles) {
            if (profile.equalsIgnoreCase("dev")) return true;
        }
        return false;
    }

    /**
     * Generates JWT token for authenticated user.

    public String generateToken(Authentication authentication, UUID userId, UUID tenantId) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(username)
                .claim(USER_ID_CLAIM, userId.toString())
                .claim(TENANT_ID_CLAIM, tenantId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates JWT token with explicit parameters.

    public String generateToken(String username, UUID userId, UUID tenantId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(username)
                .claim(USER_ID_CLAIM, userId.toString())
                .claim(TENANT_ID_CLAIM, tenantId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts username from JWT token.

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts tenant ID from JWT token.

    public UUID getTenantIdFromToken(String token) {
        String tenantIdStr = getClaims(token).get(TENANT_ID_CLAIM, String.class);
        return tenantIdStr != null ? UUID.fromString(tenantIdStr) : null;
    }

    /**
     * Extracts user ID from JWT token.

    public UUID getUserIdFromToken(String token) {
        String userIdStr = getClaims(token).get(USER_ID_CLAIM, String.class);
        return userIdStr != null ? UUID.fromString(userIdStr) : null;
    }

    /**
     * Validates JWT token.

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        }
        return false;
    }

    /**
     * Parses JWT token and returns claims.

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


 */

