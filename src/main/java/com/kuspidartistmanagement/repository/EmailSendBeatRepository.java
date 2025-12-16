package com.kuspidartistmanagement.repository;

import com.kuspidartistmanagement.domain.entity.EmailSendBeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for EmailSendBeat join entity operations.
 */
@Repository
public interface EmailSendBeatRepository extends JpaRepository<EmailSendBeat, EmailSendBeat.EmailSendBeatId> {

    void deleteByEmailSendIdAndBeatId(UUID emailSendId, UUID beatId);
}