package com.kuspidartistmanagement.dto.request;

import com.kuspidartistmanagement.domain.enums.PriorityLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistFilterRequest {

    private String genre;
    private PriorityLevel priorityLevel;
    private Set<UUID> tagIds;

    /**
     * Tag matching logic: AND or OR
     * AND: Artist must have ALL specified tags
     * OR: Artist must have ANY of the specified tags
     */
    private TagMatchMode tagMatchMode = TagMatchMode.OR;

    /**
     * Specific artist IDs (overrides filters if provided)
     */
    private Set<UUID> specificArtistIds;

    public enum TagMatchMode {
        AND, OR
    }
}