package com.kuspidartistmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackCreateRequest {

    @NotBlank(message = "Pack name is required")
    private String name;

    private String description;
    private Set<UUID> beatIds;
}