package com.kuspidartistmanagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for storage providers.
 * Supports local and S3-compatible storage.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.storage")
public class StorageConfig {

    /**
     * Storage provider type: "local" or "s3"
     */
    private String provider = "local";

    /**
     * Local storage configuration
     */
    private String localPath = "./storage/beats";

    /**
     * S3-compatible storage configuration
     */
    private S3Config s3 = new S3Config();

    @Getter
    @Setter
    public static class S3Config {
        private String endpoint;
        private String bucket;
        private String accessKey;
        private String secretKey;
        private String region = "us-east-1";
    }
}