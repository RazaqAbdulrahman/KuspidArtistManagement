package com.kuspidartistmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeatResponse {
    private UUID id;
    private String title;
    private Integer bpm;
    private String genre;
    private String mood;
    private String fileUrl;
    private Long fileSize;
    private Integer durationSeconds;
    private Boolean active;
    private LocalDateTime createdAt;
}