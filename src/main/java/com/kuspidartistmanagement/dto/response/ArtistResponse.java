package com.kuspidartistmanagement.dto.response;

import com.kuspidartistmanagement.domain.enums.PriorityLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponse {
    private UUID id;
    private String name;
    private String email;
    private String genre;
    private PriorityLevel priorityLevel;
    private Set<TagResponse> tags;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}