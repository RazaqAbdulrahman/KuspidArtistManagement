package com.kuspidartistmanagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    /**
     * JWT secret key for signing tokens
     */
    private String secret;

    /**
     * JWT expiration time in milliseconds
     */
    private long expiration;
}