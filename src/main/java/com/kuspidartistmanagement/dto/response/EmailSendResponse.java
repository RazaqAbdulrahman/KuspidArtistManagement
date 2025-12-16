package com.kuspidartistmanagement.dto.response;

import com.kuspidartistmanagement.domain.enums.EmailStatus;
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
public class EmailSendResponse {
    private UUID id;
    private UUID artistId;
    private String artistName;
    private String artistEmail;
    private UUID packId;
    private String packName;
    private Set<BeatResponse> beats;
    private EmailStatus status;
    private Integer retryCount;
    private LocalDateTime sentAt;
    private LocalDateTime failedAt;
    private String errorMessage;
    private LocalDateTime createdAt;
}