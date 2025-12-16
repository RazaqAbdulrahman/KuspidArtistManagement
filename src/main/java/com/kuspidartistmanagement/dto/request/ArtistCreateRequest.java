package com.kuspidartistmanagement.dto.request;

import com.kuspidartistmanagement.domain.enums.PriorityLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String genre;
    private PriorityLevel priorityLevel = PriorityLevel.MEDIUM;
    private Set<UUID> tagIds;
}