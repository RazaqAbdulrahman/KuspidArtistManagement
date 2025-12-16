package com.kuspidartistmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Reply entity capturing artist responses to sent emails.
 * Ingested via webhook from email service provider.
 * Linked back to original EmailSend for tracking.
 */
@Entity
@Table(name = "replies", indexes = {
        @Index(name = "idx_reply_artist", columnList = "artist_id"),
        @Index(name = "idx_reply_email_send", columnList = "email_send_id"),
        @Index(name = "idx_reply_replied_at", columnList = "replied_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "artist_id", insertable = false, updatable = false)
    private UUID artistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_send_id")
    private EmailSend emailSend;

    @Column(name = "email_send_id", insertable = false, updatable = false)
    private UUID emailSendId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "replied_at", nullable = false)
    private LocalDateTime repliedAt;

    @Column(name = "from_email", length = 255)
    private String fromEmail;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "processed", nullable = false)
    @Builder.Default
    private Boolean processed = false;

    /**
     * Creates a new reply from webhook data.
     *
     * @param artist    the artist who replied
     * @param emailSend the original email send
     * @param content   reply content
     * @param fromEmail artist's email
     * @param subject   reply subject
     * @return new Reply instance
     */
    public static Reply createReply(Artist artist, EmailSend emailSend, String content,
                                    String fromEmail, String subject) {
        return Reply.builder()
                .artist(artist)
                .emailSend(emailSend)
                .content(content)
                .fromEmail(fromEmail)
                .subject(subject)
                .repliedAt(LocalDateTime.now())
                .processed(false)
                .build();
    }

    /**
     * Marks this reply as processed.
     */
    public void markAsProcessed() {
        this.processed = true;
    }
}