package com.kuspidartistmanagement.implservice;

import com.kuspidartistmanagement.domain.entity.Artist;
import com.kuspidartistmanagement.domain.entity.Beat;
import com.kuspidartistmanagement.domain.entity.EmailSend;
import com.kuspidartistmanagement.domain.entity.Pack;
import com.kuspidartistmanagement.dto.request.SendBeatRequest;
import com.kuspidartistmanagement.dto.response.SendJobResponse;
import com.kuspidartistmanagement.email.EmailTemplate;
import com.kuspidartistmanagement.repository.*;
import com.kuspidartistmanagement.security.TenantContext;
import com.kuspidartistmanagement.service.ArtistService;
import com.kuspidartistmanagement.service.EmailSendService;
import com.kuspidartistmanagement.worker.EmailSendWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendServiceImpl implements EmailSendService {

    private final EmailSendRepository emailSendRepository;
    private final BeatRepository beatRepository;
    private final PackRepository packRepository;
    private final ArtistRepository artistRepository;
    private final TenantRepository tenantRepository;
    private final ArtistService artistService;
    private final EmailSendWorker emailSendWorker;

    @Override
    @Transactional
    public SendJobResponse sendBeatsToArtists(SendBeatRequest request) {
        UUID tenantId = getTenantId();

        // Get filtered artists
        List<Artist> artists = artistService.filterArtists(request.getArtistFilter())
                .stream()
                .map(ar -> artistRepository.findById(ar.getId()).orElseThrow())
                .collect(Collectors.toList());

        if (artists.isEmpty()) {
            return SendJobResponse.builder()
                    .artistsMatched(0)
                    .emailsQueued(0)
                    .message("No artists matched the filter criteria")
                    .build();
        }

        // Determine what to send: beats or pack
        List<Beat> beatsToSend = null;
        Pack packToSend = null;
        String subject = request.getCustomSubject();

        if (request.getPackId() != null) {
            packToSend = packRepository.findByIdAndTenantId(request.getPackId(), tenantId)
                    .orElseThrow(() -> new IllegalArgumentException("Pack not found"));
            if (subject == null) {
                subject = EmailTemplate.buildDefaultSubject(true);
            }
        } else if (request.getBeatIds() != null && !request.getBeatIds().isEmpty()) {
            beatsToSend = beatRepository.findAllById(request.getBeatIds());
            if (subject == null) {
                subject = EmailTemplate.buildDefaultSubject(false);
            }
        } else {
            throw new IllegalArgumentException("Either beatIds or packId must be provided");
        }

        // Create email sends for each artist
        int emailsQueued = 0;
        for (Artist artist : artists) {
            EmailSend emailSend = createEmailSend(artist, beatsToSend, packToSend, subject, request.getCustomMessage());
            emailSend = emailSendRepository.save(emailSend);

            // Queue for async sending
            emailSendWorker.sendEmail(emailSend.getId());
            emailsQueued++;
        }

        UUID jobId = UUID.randomUUID();
        log.info("Email send job created: {} emails queued for {} artists", emailsQueued, artists.size());

        return SendJobResponse.builder()
                .artistsMatched(artists.size())
                .emailsQueued(emailsQueued)
                .message(String.format("Successfully queued %d emails", emailsQueued))
                .jobId(jobId)
                .build();
    }

    private EmailSend createEmailSend(Artist artist, List<Beat> beats, Pack pack, String subject, String customMessage) {
        UUID tenantId = getTenantId();

        EmailSend.EmailSendBuilder builder = EmailSend.builder()
                .artist(artist)
                .tenant(tenantRepository.findById(tenantId).orElseThrow())
                .subject(subject);

        if (pack != null) {
            builder.pack(pack);
            String body = EmailTemplate.buildPackEmailBody(artist, pack, customMessage);
            builder.emailBody(body);
        } else if (beats != null) {
            String body = EmailTemplate.buildBeatEmailBody(artist, beats, customMessage);
            builder.emailBody(body);
        }

        EmailSend emailSend = builder.build();

        if (beats != null) {
            beats.forEach(emailSend::addBeat);
        }

        return emailSend;
    }

    private UUID getTenantId() {
        UUID tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context not set");
        }
        return tenantId;
    }
}