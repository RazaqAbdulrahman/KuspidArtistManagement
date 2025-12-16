package com.kuspidartistmanagement.util;

public final class Constants {
    private Constants() {
        // Prevent instantiation
    }

    // Email Templates
    public static final String DEFAULT_EMAIL_SUBJECT = "New Beats from Kuspid";
    public static final String DEFAULT_EMAIL_TEMPLATE = """
            Hi %s,
            
            We have some fresh beats that we think you'll love!
            
            %s
            
            Check them out and let us know what you think.
            
            Best regards,
            Kuspid Team
            """;

    // File Upload
    public static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB
    public static final String[] ALLOWED_AUDIO_FORMATS = {".mp3", ".wav", ".flac", ".m4a"};

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    // Security
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}