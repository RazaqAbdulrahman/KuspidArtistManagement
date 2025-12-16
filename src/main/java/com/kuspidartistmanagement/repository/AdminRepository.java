package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Admin entity operations.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByEmailAndActiveTrue(String email);

    boolean existsByEmail(String email);

    Optional<Admin> findByIdAndTenantId(UUID id, UUID tenantId);
}