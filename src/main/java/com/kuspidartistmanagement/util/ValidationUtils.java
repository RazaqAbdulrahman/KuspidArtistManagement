package com.kuspidartistmanagement.util;

import java.util.regex.Pattern;

public final class ValidationUtils {
    private ValidationUtils() {
        // Prevent instantiation
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern FILE_EXTENSION_PATTERN = Pattern.compile(
            "\\.(mp3|wav|flac|m4a)$", Pattern.CASE_INSENSITIVE
    );

    /**
     * Validates email format.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates audio file extension.
     */
    public static boolean isValidAudioFile(String filename) {
        return filename != null && FILE_EXTENSION_PATTERN.matcher(filename).find();
    }

    /**
     * Validates UUID format.
     */
    public static boolean isValidUUID(String uuid) {
        if (uuid == null) {
            return false;
        }
        try {
            java.util.UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Sanitizes string input by trimming and removing null bytes.
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("\\x00", "");
    }

    /**
     * Validates BPM range (typically 60-200 for music).
     */
    public static boolean isValidBPM(Integer bpm) {
        return bpm != null && bpm >= 40 && bpm <= 300;
    }
}