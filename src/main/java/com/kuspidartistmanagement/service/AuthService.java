package com.kuspidartistmanagement.service;

import com.kuspidartistmanagement.dto.request.LoginRequest;
import com.kuspidartistmanagement.dto.response.LoginResponse;

/**
 * Authentication service interface.
 */
public interface AuthService {
    LoginResponse login(LoginRequest request);
}