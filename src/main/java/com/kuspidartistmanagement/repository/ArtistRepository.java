package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.Artist;
import com.kuspidartistmanagement.domain.enums.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Repository for Artist entity with advanced filtering capabilities.
 */
@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    List<Artist> findByTenantIdAndActiveTrue(UUID tenantId);

    Optional<Artist> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<Artist> findByEmailAndTenantId(String email, UUID tenantId);

    List<Artist> findByTenantIdAndGenre(UUID tenantId, String genre);

    List<Artist> findByTenantIdAndPriorityLevel(UUID tenantId, PriorityLevel priorityLevel);

    boolean existsByEmailAndTenantId(String email, UUID tenantId);

    /**
     * Finds artists by tenant and tag IDs with AND logic (artist must have ALL tags).
     *
     * @param tenantId tenant identifier
     * @param tagIds   set of tag identifiers
     * @param tagCount number of tags (must match for AND logic)
     * @return list of matching artists
     */
    @Query("SELECT DISTINCT a FROM Artist a " +
            "JOIN a.artistTags at " +
            "WHERE a.tenantId = :tenantId " +
            "AND at.tag.id IN :tagIds " +
            "AND a.active = true " +
            "GROUP BY a.id " +
            "HAVING COUNT(DISTINCT at.tag.id) = :tagCount")
    List<Artist> findByTenantIdAndAllTags(
            @Param("tenantId") UUID tenantId,
            @Param("tagIds") Set<UUID> tagIds,
            @Param("tagCount") long tagCount);

    /**
     * Finds artists by tenant and tag IDs with OR logic (artist has ANY of the tags).
     *
     * @param tenantId tenant identifier
     * @param tagIds   set of tag identifiers
     * @return list of matching artists
     */
    @Query("SELECT DISTINCT a FROM Artist a " +
            "JOIN a.artistTags at " +
            "WHERE a.tenantId = :tenantId " +
            "AND at.tag.id IN :tagIds " +
            "AND a.active = true")
    List<Artist> findByTenantIdAndAnyTags(
            @Param("tenantId") UUID tenantId,
            @Param("tagIds") Set<UUID> tagIds);

    /**
     * Complex filter query supporting genre, priority, and tag combinations.
     *
     * @param tenantId      tenant identifier
     * @param genre         genre filter (nullable)
     * @param priorityLevel priority filter (nullable)
     * @param tagIds        tag filter (nullable)
     * @return list of matching artists
     */
    @Query("SELECT DISTINCT a FROM Artist a " +
            "LEFT JOIN a.artistTags at " +
            "WHERE a.tenantId = :tenantId " +
            "AND a.active = true " +
            "AND (:genre IS NULL OR a.genre = :genre) " +
            "AND (:priorityLevel IS NULL OR a.priorityLevel = :priorityLevel) " +
            "AND (:tagIds IS NULL OR at.tag.id IN :tagIds)")
    List<Artist> findByComplexFilter(
            @Param("tenantId") UUID tenantId,
            @Param("genre") String genre,
            @Param("priorityLevel") PriorityLevel priorityLevel,
            @Param("tagIds") Set<UUID> tagIds);
}