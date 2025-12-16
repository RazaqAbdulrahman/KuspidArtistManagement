package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.PackBeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for PackBeat join entity operations.
 */
@Repository
public interface PackBeatRepository extends JpaRepository<PackBeat, PackBeat.PackBeatId> {

    void deleteByPackIdAndBeatId(UUID packId, UUID beatId);
}