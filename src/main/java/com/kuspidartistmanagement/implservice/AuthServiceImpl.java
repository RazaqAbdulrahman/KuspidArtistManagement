/*
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        log.debug("Attempting login for user: {}", request.getEmail());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            log.warn("Login failed for user: {} - invalid credentials", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Admin admin = userDetailsService.loadAdminByEmail(request.getEmail());

        String token = jwtTokenProvider.generateToken(
                authentication,
                admin.getId(),
                admin.getTenantId()
        );

        log.info("User logged in successfully: {}", request.getEmail());

        return LoginResponse.builder()
                .accessToken(token)
                .type("Bearer")
                .userId(admin.getId())
                .email(admin.getEmail())
                .tenantId(admin.getTenantId())
                .build();
    }
}


 */
