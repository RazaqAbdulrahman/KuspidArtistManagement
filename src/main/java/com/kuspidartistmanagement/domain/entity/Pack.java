package com.kuspidartistmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Pack entity representing a collection of beats.
 * Treated as a single sendable unit with aggregated analytics.
 * Tenant-scoped for data isolation.
 */
@Entity
@Table(name = "packs", indexes = {
        @Index(name = "idx_pack_tenant", columnList = "tenant_id"),
        @Index(name = "idx_pack_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pack extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false)
    private UUID tenantId;

    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PackBeat> packBeats = new HashSet<>();

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    /**
     * Creates a new pack for a tenant.
     *
     * @param name   pack name
     * @param tenant tenant association
     * @return new Pack instance
     */
    public static Pack createPack(String name, Tenant tenant) {
        return Pack.builder()
                .name(name)
                .tenant(tenant)
                .active(true)
                .build();
    }

    /**
     * Adds a beat to this pack.
     *
     * @param beat the beat to add
     */
    public void addBeat(Beat beat) {
        PackBeat packBeat = new PackBeat(this, beat);
        packBeats.add(packBeat);
    }

    /**
     * Removes a beat from this pack.
     *
     * @param beat the beat to remove
     */
    public void removeBeat(Beat beat) {
        packBeats.removeIf(pb -> pb.getBeat().equals(beat));
    }

    /**
     * Gets the count of beats in this pack.
     *
     * @return number of beats
     */
    public int getBeatCount() {
        return packBeats.size();
    }
}