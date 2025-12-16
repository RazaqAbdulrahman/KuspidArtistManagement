package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Tenant entity operations.
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    Optional<Tenant> findByEmail(String email);

    Optional<Tenant> findBySubdomain(String subdomain);

    boolean existsByEmail(String email);
}