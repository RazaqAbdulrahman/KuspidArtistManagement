package com.kuspidartistmanagement.domain.entity;

import com.kuspidartistmanagement.domain.enums.PriorityLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Artist entity representing musicians/producers receiving beats.
 * Multi-tag support for flexible categorization.
 * Priority-based filtering for targeted campaigns.
 */
@Entity
@Table(name = "artists", indexes = {
        @Index(name = "idx_artist_email", columnList = "email"),
        @Index(name = "idx_artist_tenant", columnList = "tenant_id"),
        @Index(name = "idx_artist_priority", columnList = "priority_level"),
        @Index(name = "idx_artist_genre", columnList = "genre")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "genre", length = 100)
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level", nullable = false, length = 20)
    @Builder.Default
    private PriorityLevel priorityLevel = PriorityLevel.MEDIUM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false)
    private UUID tenantId;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ArtistTag> artistTags = new HashSet<>();

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    /**
     * Adds a tag to this artist.
     *
     * @param tag the tag to associate
     */
    public void addTag(Tag tag) {
        ArtistTag artistTag = new ArtistTag(this, tag);
        artistTags.add(artistTag);
    }

    /**
     * Removes a tag from this artist.
     *
     * @param tag the tag to remove
     */
    public void removeTag(Tag tag) {
        artistTags.removeIf(at -> at.getTag().equals(tag));
    }

    /**
     * Updates priority level.
     *
     * @param newPriority the new priority level
     */
    public void updatePriority(PriorityLevel newPriority) {
        this.priorityLevel = newPriority;
    }
}