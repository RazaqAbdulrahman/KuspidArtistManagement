/*
package com.kuspidartistmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * JWT authentication filter for processing authentication tokens.
 * Extracts JWT from request, validates it, and sets Spring Security context.
 * Also extracts and sets tenant context for multi-tenancy.
 *
 * Flow:
 * 1. Extract JWT token from Authorization header
 * 2. Validate token
 * 3. Extract username and tenant ID
 * 4. Load user details
 * 5. Set authentication in SecurityContext
 * 6. Set tenant in TenantContext
 * 7. Clear tenant context after request completes
 *
 * Thread-safe - uses constructor injection, no mutable state.

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                authenticateUser(jwt, request);
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
            filterChain.doFilter(request, response);
        } finally {
            // Critical: Clear tenant context to prevent leakage between requests
            TenantContext.clear();
        }
    }

    /**
     * Extracts JWT token from Authorization header.
     *
     * @param request HTTP request
     * @return JWT token or null if not found

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * Authenticates user based on JWT token.
     * Sets both Spring Security context and Tenant context.
     *
     * @param jwt     JWT token
     * @param request HTTP request

    private void authenticateUser(String jwt, HttpServletRequest request) {
        // Extract username from token
        String username = jwtTokenProvider.getUsernameFromToken(jwt);

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Create authentication token
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Set authentication in Spring Security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Extract and set tenant context
        UUID tenantId = jwtTokenProvider.getTenantIdFromToken(jwt);
        if (tenantId != null) {
            TenantContext.setTenantId(tenantId);
            log.debug("Set tenant context: {} for user: {}", tenantId, username);
        } else {
            log.warn("No tenant ID found in JWT for user: {}", username);
        }
    }
}


 */
