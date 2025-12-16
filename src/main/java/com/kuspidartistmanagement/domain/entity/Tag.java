package com.kuspidartistmanagement.domain.entity;

import com.kuspidartistmanagement.domain.enums.TagType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Tag entity for artist categorization.
 * Supports free-form, predefined, and AI-suggested tags.
 * Tenant-scoped for data isolation.
 */
@Entity
@Table(name = "tags",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "tenant_id"}),
        indexes = {
                @Index(name = "idx_tag_name", columnList = "name"),
                @Index(name = "idx_tag_type", columnList = "type"),
                @Index(name = "idx_tag_tenant", columnList = "tenant_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    @Builder.Default
    private TagType type = TagType.CUSTOM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false)
    private UUID tenantId;

    /**
     * Creates a new tag for a tenant.
     *
     * @param name   tag name
     * @param type   tag type
     * @param tenant tenant association
     * @return new Tag instance
     */
    public static Tag createTag(String name, TagType type, Tenant tenant) {
        return Tag.builder()
                .name(name.toLowerCase().trim())
                .type(type)
                .tenant(tenant)
                .build();
    }
}