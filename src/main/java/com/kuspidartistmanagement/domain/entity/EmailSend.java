package com.kuspidartistmanagement.domain.entity;

import com.kuspidartistmanagement.domain.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * EmailSend entity tracking email delivery to artists.
 * Supports retry logic and status tracking.
 * Links to beats/packs sent in the email.
 */
@Entity
@Table(name = "email_sends", indexes = {
        @Index(name = "idx_email_send_artist", columnList = "artist_id"),
        @Index(name = "idx_email_send_tenant", columnList = "tenant_id"),
        @Index(name = "idx_email_send_status", columnList = "status"),
        @Index(name = "idx_email_send_sent_at", columnList = "sent_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailSend extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "artist_id", insertable = false, updatable = false)
    private UUID artistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id")
    private Pack pack;

    @Column(name = "pack_id", insertable = false, updatable = false)
    private UUID packId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private EmailStatus status = EmailStatus.PENDING;

    @Column(name = "retry_count", nullable = false)
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "email_body", columnDefinition = "TEXT")
    private String emailBody;

    @OneToMany(mappedBy = "emailSend", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<EmailSendBeat> emailSendBeats = new HashSet<>();

    @OneToMany(mappedBy = "emailSend", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Reply> replies = new HashSet<>();

    /**
     * Marks email as successfully sent.
     */
    public void markAsSent() {
        this.status = EmailStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.errorMessage = null;
    }

    /**
     * Marks email as failed with error message.
     *
     * @param errorMessage the error description
     */
    public void markAsFailed(String errorMessage) {
        this.status = EmailStatus.FAILED;
        this.failedAt = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

    /**
     * Increments retry count and marks for retry.
     */
    public void incrementRetry() {
        this.retryCount++;
        this.status = EmailStatus.RETRY;
    }

    /**
     * Checks if max retries have been exceeded.
     *
     * @param maxRetries maximum allowed retries
     * @return true if should not retry anymore
     */
    public boolean hasExceededMaxRetries(int maxRetries) {
        return this.retryCount >= maxRetries;
    }

    /**
     * Adds a beat to this email send.
     *
     * @param beat the beat to associate
     */
    public void addBeat(Beat beat) {
        EmailSendBeat emailSendBeat = new EmailSendBeat(this, beat);
        emailSendBeats.add(emailSendBeat);
    }
}