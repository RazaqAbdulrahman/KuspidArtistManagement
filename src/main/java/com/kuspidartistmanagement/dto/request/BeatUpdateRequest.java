package com.kuspidartistmanagement.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeatUpdateRequest {

    private String title;

    @Positive(message = "BPM must be positive")
    private Integer bpm;

    private String genre;
    private String mood;
    private Boolean active;
}