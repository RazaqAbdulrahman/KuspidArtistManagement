package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.Pack;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Pack entity operations with analytics support.
 */
@Repository
public interface PackRepository extends JpaRepository<Pack, UUID> {

    // Find all active packs for a tenant
    List<Pack> findByTenantIdAndActiveTrue(UUID tenantId);

    // Find a pack by ID and tenant ID
    Optional<Pack> findByIdAndTenantId(UUID id, UUID tenantId);

    // Check if a pack exists by ID and tenant ID
    boolean existsByIdAndTenantId(UUID id, UUID tenantId);

    /**
     * Finds the most sent packs for a tenant.
     * Use Pageable to limit the number of results.
     *
     * @param tenantId tenant identifier
     * @param pageable Pageable object controlling limit and sorting
     * @return list of pack IDs with send counts
     */
    @Query("SELECT p.id, COUNT(es.pack.id) as sendCount " +
            "FROM Pack p " +
            "JOIN EmailSend es ON es.pack.id = p.id " +
            "WHERE p.tenantId = :tenantId " +
            "GROUP BY p.id " +
            "ORDER BY sendCount DESC")
    List<Object[]> findMostSentPacks(@Param("tenantId") UUID tenantId, Pageable pageable);
}
