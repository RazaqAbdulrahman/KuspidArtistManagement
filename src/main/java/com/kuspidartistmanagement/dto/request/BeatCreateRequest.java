package com.kuspidartistmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeatCreateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @Positive(message = "BPM must be positive")
    private Integer bpm;

    private String genre;
    private String mood;

    // File will be uploaded separately and URL set by service
}