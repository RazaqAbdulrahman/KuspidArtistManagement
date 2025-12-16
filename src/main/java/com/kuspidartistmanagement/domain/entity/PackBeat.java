package com.kuspidartistmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Join entity for Pack-Beat many-to-many relationship.
 * Allows beats to belong to multiple packs.
 */
@Entity
@Table(name = "pack_beats", indexes = {
        @Index(name = "idx_pack_beat_pack", columnList = "pack_id"),
        @Index(name = "idx_pack_beat_beat", columnList = "beat_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"pack", "beat"})
public class PackBeat implements Serializable {

    @EmbeddedId
    private PackBeatId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("packId")
    @JoinColumn(name = "pack_id")
    private Pack pack;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("beatId")
    @JoinColumn(name = "beat_id")
    private Beat beat;

    @Column(name = "position")
    private Integer position;

    public PackBeat(Pack pack, Beat beat) {
        this.pack = pack;
        this.beat = beat;
        this.id = new PackBeatId(pack.getId(), beat.getId());
    }

    /**
     * Composite primary key for PackBeat.
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class PackBeatId implements Serializable {
        @Column(name = "pack_id")
        private UUID packId;

        @Column(name = "beat_id")
        private UUID beatId;
    }
}