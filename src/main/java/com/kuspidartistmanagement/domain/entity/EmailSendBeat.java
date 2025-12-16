package com.kuspidartistmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Join entity linking EmailSend to individual Beats.
 * Allows tracking which specific beats were sent in each email.
 */
@Entity
@Table(name = "email_send_beats", indexes = {
        @Index(name = "idx_email_send_beat_send", columnList = "email_send_id"),
        @Index(name = "idx_email_send_beat_beat", columnList = "beat_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"emailSend", "beat"})
public class EmailSendBeat implements Serializable {

    @EmbeddedId
    private EmailSendBeatId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("emailSendId")
    @JoinColumn(name = "email_send_id")
    private EmailSend emailSend;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("beatId")
    @JoinColumn(name = "beat_id")
    private Beat beat;

    public EmailSendBeat(EmailSend emailSend, Beat beat) {
        this.emailSend = emailSend;
        this.beat = beat;
        this.id = new EmailSendBeatId(emailSend.getId(), beat.getId());
    }

    /**
     * Composite primary key for EmailSendBeat.
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class EmailSendBeatId implements Serializable {
        @Column(name = "email_send_id")
        private UUID emailSendId;

        @Column(name = "beat_id")
        private UUID beatId;
    }
}