package com.kuspidartistmanagement.domain.enums;

/**
 * Tag classification types.
 * FREE: User-created custom tags
 * PREDEFINED: System-defined tags
 * SUGGESTED: AI/system-assisted tags for auto-completion
 */
public enum TagType {
    GENRE,
    MOOD,
    STYLE,
    CUSTOM,
    LOCATION,
    PLATFORM
}