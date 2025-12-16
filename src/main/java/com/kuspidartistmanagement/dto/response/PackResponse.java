package com.kuspidartistmanagement.dto.response;

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
public class PackResponse {
    private UUID id;
    private String name;
    private String description;
    private Set<BeatResponse> beats;
    private Integer beatCount;
    private Boolean active;
    private LocalDateTime createdAt;
    private UUID tenantId;
}