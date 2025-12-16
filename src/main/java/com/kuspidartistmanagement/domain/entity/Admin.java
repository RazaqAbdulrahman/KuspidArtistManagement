package com.kuspidartistmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Admin entity representing the producer/administrator user.
 * In v1: single admin per tenant.
 * Future: multiple team members with role-based access.
 * Password is stored hashed (BCrypt).
 */
@Entity
@Table(name = "admins", indexes = {
        @Index(name = "idx_admin_email", columnList = "email"),
        @Index(name = "idx_admin_tenant", columnList = "tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false)
    private UUID tenantId;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    /**
     * Creates a new admin associated with a tenant.
     *
     * @param email        admin email
     * @param password     hashed password
     * @param firstName    first name
     * @param lastName     last name
     * @param tenant       tenant association
     * @return new Admin instance
     */
    public static Admin createAdmin(String email, String password, String firstName,
                                    String lastName, Tenant tenant) {
        return Admin.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .tenant(tenant)
                .active(true)
                .build();
    }

    /**
     * Gets the full name of the admin.
     *
     * @return concatenated first and last name
     */
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return email;
        }
        return String.format("%s %s",
                firstName != null ? firstName : "",
                lastName != null ? lastName : "").trim();
    }

    /**
     * Deactivates this admin account.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Reactivates this admin account.
     */
    public void activate() {
        this.active = true;
    }
}