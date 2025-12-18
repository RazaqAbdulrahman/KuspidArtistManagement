package com.kuspidartistmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken; // match frontend naming
    private String tokenType;   // usually "Bearer", optional
    private UUID userId;
    private String email;
    private UUID tenantId;

}
