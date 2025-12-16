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
public class ReplyResponse {
    private UUID id;
    private UUID artistId;
    private String artistName;
    private UUID emailSendId;
    private String content;
    private String fromEmail;
    private String subject;
    private LocalDateTime repliedAt;
    private Boolean processed;
}