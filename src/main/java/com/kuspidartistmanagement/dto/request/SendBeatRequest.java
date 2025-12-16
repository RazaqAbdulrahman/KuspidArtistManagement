package com.kuspidartistmanagement.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendBeatRequest {

    // Either beatIds or packId must be provided
    private Set<UUID> beatIds;
    private UUID packId;

    @NotNull(message = "Artist filter is required")
    private ArtistFilterRequest artistFilter;

    private String customSubject;
    private String customMessage;
}