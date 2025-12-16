package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.Beat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Beat entity operations with analytics support.
 */
@Repository
public interface BeatRepository extends JpaRepository<Beat, UUID> {

    // Find all active beats for a tenant
    List<Beat> findByTenantIdAndActiveTrue(UUID tenantId);

    // Find a beat by ID and tenant ID
    Optional<Beat> findByIdAndTenantId(UUID id, UUID tenantId);

    // Find beats by tenant and genre
    List<Beat> findByTenantIdAndGenre(UUID tenantId, String genre);

    // Check if a beat exists by ID and tenant ID
    boolean existsByIdAndTenantId(UUID id, UUID tenantId);

    /**
     * Finds the most sent beats for a tenant.
     * Use Pageable to limit the number of results.
     *
     * @param tenantId tenant identifier
     * @param pageable Pageable object controlling limit and sorting
     * @return list of beat IDs with send counts
     */
    @Query("SELECT b.id, COUNT(esb.beat.id) as sendCount " +
            "FROM Beat b " +
            "JOIN EmailSendBeat esb ON esb.beat.id = b.id " +
            "JOIN EmailSend es ON esb.emailSend.id = es.id " +
            "WHERE b.tenantId = :tenantId " +
            "GROUP BY b.id " +
            "ORDER BY sendCount DESC")
    List<Object[]> findMostSentBeats(@Param("tenantId") UUID tenantId, Pageable pageable);
}
