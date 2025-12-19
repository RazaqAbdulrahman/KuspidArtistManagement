/*
package com.kuspidartistmanagement.security;

import com.kuspidartistmanagement.domain.entity.Admin;
import com.kuspidartistmanagement.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Custom UserDetailsService for loading admin users.
 * Integrates with Spring Security for authentication.
 * Loads user from database and converts to Spring Security UserDetails.
 *
 * Thread-safe and stateless - uses constructor injection for testability.

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    /**
     * Loads user by email (username in our system).
     * Required by Spring Security authentication flow.
     *
     * @param username the email/username
     * @return UserDetails for Spring Security
     * @throws UsernameNotFoundException if user not found

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", username);

        Admin admin = adminRepository.findByEmailAndActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + username));

        return buildUserDetails(admin);
    }

    /**
     * Converts Admin entity to Spring Security UserDetails.
     * In v1, all admins have the same role.
     * Future: implement role-based access control.
     *
     * @param admin the admin entity
     * @return UserDetails for Spring Security

    private UserDetails buildUserDetails(Admin admin) {
        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_ADMIN")))
                .accountExpired(false)
                .accountLocked(!admin.getActive())
                .credentialsExpired(false)
                .disabled(!admin.getActive())
                .build();
    }

    /**
     * Loads admin entity by email for additional processing.
     * Used when we need full Admin entity (e.g., for JWT generation).
     *
     * @param email the admin email
     * @return Admin entity
     * @throws UsernameNotFoundException if user not found

    @Transactional(readOnly = true)
    public Admin loadAdminByEmail(String email) {
        return adminRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email));
    }
}


 */


