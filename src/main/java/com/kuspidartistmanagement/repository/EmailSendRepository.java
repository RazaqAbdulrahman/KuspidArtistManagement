package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.EmailSend;
import com.kuspidartistmanagement.domain.enums.EmailStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for EmailSend entity with retry and analytics support.
 */
@Repository
public interface EmailSendRepository extends JpaRepository<EmailSend, UUID> {

    List<EmailSend> findByTenantIdAndStatus(UUID tenantId, EmailStatus status);

    List<EmailSend> findByStatusAndRetryCountLessThan(EmailStatus status, int maxRetries);

    List<EmailSend> findByArtistIdAndTenantId(UUID artistId, UUID tenantId);

    /**
     * Finds emails ready for retry (status RETRY and last update time passed).
     */
    @Query("SELECT es FROM EmailSend es " +
            "WHERE es.status = :status " +
            "AND es.retryCount < :maxRetries " +
            "AND es.updatedAt < :retryAfter")
    List<EmailSend> findEmailsForRetry(
            @Param("status") EmailStatus status,
            @Param("maxRetries") int maxRetries,
            @Param("retryAfter") LocalDateTime retryAfter);

    @Query("SELECT COUNT(es) FROM EmailSend es WHERE es.tenantId = :tenantId")
    long countByTenantId(@Param("tenantId") UUID tenantId);

    @Query("SELECT COUNT(es) FROM EmailSend es " +
            "WHERE es.tenantId = :tenantId AND es.status = :status")
    long countByTenantIdAndStatus(@Param("tenantId") UUID tenantId, @Param("status") EmailStatus status);

    /**
     * Finds most contacted artists by tenant with limit.
     * Use Pageable to control the maximum results.
     */
    @Query("SELECT es.artist.id, COUNT(es.id) as sendCount " +
            "FROM EmailSend es " +
            "WHERE es.tenantId = :tenantId " +
            "GROUP BY es.artist.id " +
            "ORDER BY sendCount DESC")
    List<Object[]> findMostContactedArtists(@Param("tenantId") UUID tenantId, Pageable pageable);

    @Query("SELECT COUNT(es) FROM EmailSend es " +
            "WHERE es.tenantId = :tenantId " +
            "AND es.createdAt BETWEEN :startDate AND :endDate")
    long countByTenantIdAndDateRange(
            @Param("tenantId") UUID tenantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
