package com.kuspidartistmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Tenant entity representing an organization/producer account.
 * Multi-tenancy ready: all data is scoped by tenant.
 * In v1, only one tenant exists, but architecture supports SaaS expansion.
 */
@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "subdomain", unique = true, length = 100)
    private String subdomain;

    /**
     * Creates a new active tenant with the given name and email.
     *
     * @param name  tenant/producer name
     * @param email tenant contact email
     * @return new Tenant instance
     */
    public static Tenant createTenant(String name, String email) {
        return Tenant.builder()
                .name(name)
                .email(email)
                .active(true)
                .build();
    }

    /**
     * Deactivates this tenant, preventing further operations.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Reactivates this tenant.
     */
    public void activate() {
        this.active = true;
    }
}