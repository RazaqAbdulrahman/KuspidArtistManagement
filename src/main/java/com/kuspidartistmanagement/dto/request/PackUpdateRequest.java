package com.kuspidartistmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackUpdateRequest {

    private String name;
    private String description;
    private Set<UUID> beatIds;
    private Boolean active;
}