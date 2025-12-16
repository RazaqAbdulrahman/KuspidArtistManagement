package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.Tag;
import com.kuspidartistmanagement.domain.enums.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Tag entity operations.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    List<Tag> findByTenantId(UUID tenantId);

    List<Tag> findByTenantIdAndType(UUID tenantId, TagType type);

    Optional<Tag> findByNameAndTenantId(String name, UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    /**
     * Finds tags by name prefix for auto-complete functionality.
     *
     * @param tenantId tenant identifier
     * @param prefix   tag name prefix
     * @return list of matching tags
     */
    @Query("SELECT t FROM Tag t " +
            "WHERE t.tenantId = :tenantId " +
            "AND LOWER(t.name) LIKE LOWER(CONCAT(:prefix, '%')) " +
            "ORDER BY t.name")
    List<Tag> findByNameStartingWithIgnoreCase(
            @Param("tenantId") UUID tenantId,
            @Param("prefix") String prefix);
}