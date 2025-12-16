package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.ArtistTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for ArtistTag join entity operations.
 */
@Repository
public interface ArtistTagRepository extends JpaRepository<ArtistTag, ArtistTag.ArtistTagId> {

    void deleteByArtistIdAndTagId(UUID artistId, UUID tagId);
}