package com.kuspidartistmanagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for email sending.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.email")
public class EmailConfig {

    /**
     * Email provider type: "smtp" (default)
     */
    private String provider = "smtp";

    /**
     * Maximum retry attempts for failed emails
     */
    private int retryMax = 3;

    /**
     * Delay between retries in milliseconds (default: 5 minutes)
     */
    private long retryDelay = 300000;

    /**
     * From address for sent emails
     */
    private String fromAddress;

    /**
     * From name for sent emails
     */
    private String fromName = "Kuspid Artist Management";

    /**
     * Reply-to address for artist replies
     */
    private String replyToAddress;
}