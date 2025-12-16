package com.kuspidartistmanagement.dto.request;

import com.kuspidartistmanagement.domain.enums.PriorityLevel;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistUpdateRequest {

    private String name;

    @Email(message = "Email must be valid")
    private String email;

    private String genre;
    private PriorityLevel priorityLevel;
    private Set<UUID> tagIds;
    private Boolean active;
}