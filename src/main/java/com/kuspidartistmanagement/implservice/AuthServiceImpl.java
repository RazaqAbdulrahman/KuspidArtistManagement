
package com.kuspidartistmanagement.implservice;

import com.kuspidartistmanagement.domain.entity.Admin;
import com.kuspidartistmanagement.dto.request.LoginRequest;
import com.kuspidartistmanagement.dto.response.LoginResponse;
import com.kuspidartistmanagement.security.CustomUserDetailsService;
import com.kuspidartistmanagement.security.JwtTokenProvider;
import com.kuspidartistmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication service implementation.
 * Handles login and JWT token generation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Authenticates user and returns JWT token.
     */
    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        log.debug("Attempting login for user: {}", request.getEmail());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Load full admin entity to get tenant ID
        Admin admin = userDetailsService.loadAdminByEmail(request.getEmail());

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(
                authentication,
                admin.getId(),
                admin.getTenantId()
        );

        log.info("User logged in successfully: {}", request.getEmail());

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(admin.getId())
                .email(admin.getEmail())
                .tenantId(admin.getTenantId())
                .build();
    }
}