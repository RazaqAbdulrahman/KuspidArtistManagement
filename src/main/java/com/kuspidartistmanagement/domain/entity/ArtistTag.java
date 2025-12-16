package com.kuspidartistmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Join entity for Artist-Tag many-to-many relationship.
 * Uses composite key for efficient querying.
 */
@Entity
@Table(name = "artist_tags", indexes = {
        @Index(name = "idx_artist_tag_artist", columnList = "artist_id"),
        @Index(name = "idx_artist_tag_tag", columnList = "tag_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"artist", "tag"})
public class ArtistTag implements Serializable {

    @EmbeddedId
    private ArtistTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistId")
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public ArtistTag(Artist artist, Tag tag) {
        this.artist = artist;
        this.tag = tag;
        this.id = new ArtistTagId(artist.getId(), tag.getId());
    }

    /**
     * Composite primary key for ArtistTag.
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ArtistTagId implements Serializable {
        @Column(name = "artist_id")
        private UUID artistId;

        @Column(name = "tag_id")
        private UUID tagId;
    }
}