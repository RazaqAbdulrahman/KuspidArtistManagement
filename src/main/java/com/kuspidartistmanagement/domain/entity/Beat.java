package com.kuspidartistmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Beat entity representing a music production file.
 * Stores metadata and file reference (URL/path handled by storage layer).
 * Tenant-scoped for data isolation.
 */
@Entity
@Table(name = "beats", indexes = {
        @Index(name = "idx_beat_tenant", columnList = "tenant_id"),
        @Index(name = "idx_beat_genre", columnList = "genre"),
        @Index(name = "idx_beat_title", columnList = "title")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beat extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "bpm")
    private Integer bpm;

    @Column(name = "genre", length = 100)
    private String genre;

    @Column(name = "mood", length = 100)
    private String mood;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false)
    private UUID tenantId;

    @OneToMany(mappedBy = "beat", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<PackBeat> packBeats = new HashSet<>();

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    /**
     * Creates a new beat for a tenant.
     *
     * @param title    beat title
     * @param bpm      beats per minute
     * @param genre    musical genre
     * @param fileUrl  storage file reference
     * @param tenant   tenant association
     * @return new Beat instance
     */
    public static Beat createBeat(String title, Integer bpm, String genre,
                                  String fileUrl, Tenant tenant) {
        return Beat.builder()
                .title(title)
                .bpm(bpm)
                .genre(genre)
                .fileUrl(fileUrl)
                .tenant(tenant)
                .active(true)
                .build();
    }

    /**
     * Updates beat metadata.
     *
     * @param title beat title
     * @param bpm   beats per minute
     * @param genre musical genre
     * @param mood  mood/vibe
     */
    public void updateMetadata(String title, Integer bpm, String genre, String mood) {
        this.title = title;
        this.bpm = bpm;
        this.genre = genre;
        this.mood = mood;
    }
}