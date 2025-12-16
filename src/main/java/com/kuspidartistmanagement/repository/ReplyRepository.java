package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Reply entity with analytics support.
 */
@Repository
public interface ReplyRepository extends JpaRepository<Reply, UUID> {

    List<Reply> findByArtistId(UUID artistId);

    List<Reply> findByEmailSendId(UUID emailSendId);

    List<Reply> findByProcessedFalse();

    Optional<Reply> findByFromEmailAndEmailSendId(String fromEmail, UUID emailSendId);

    /**
     * Calculates reply rate for a specific artist.
     *
     * @param artistId artist identifier
     * @return reply count
     */
    @Query("SELECT COUNT(r) FROM Reply r WHERE r.artist.id = :artistId")
    long countByArtistId(@Param("artistId") UUID artistId);

    /**
     * Counts replies for a beat across all sends.
     *
     * @param beatId beat identifier
     * @return reply count
     */
    @Query("SELECT COUNT(r) FROM Reply r " +
            "JOIN EmailSend es ON r.emailSend.id = es.id " +
            "JOIN EmailSendBeat esb ON esb.emailSend.id = es.id " +
            "WHERE esb.beat.id = :beatId")
    long countRepliesByBeatId(@Param("beatId") UUID beatId);
}