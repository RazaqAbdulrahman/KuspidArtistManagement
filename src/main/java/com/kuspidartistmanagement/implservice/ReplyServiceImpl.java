package com.kuspidartistmanagement.implservice;

import com.kuspidartistmanagement.domain.entity.Artist;
import com.kuspidartistmanagement.domain.entity.EmailSend;
import com.kuspidartistmanagement.domain.entity.Reply;
import com.kuspidartistmanagement.dto.response.ReplyResponse;
import com.kuspidartistmanagement.exception.ResourceNotFoundException;
import com.kuspidartistmanagement.repository.ArtistRepository;
import com.kuspidartistmanagement.repository.EmailSendRepository;
import com.kuspidartistmanagement.repository.ReplyRepository;
import com.kuspidartistmanagement.security.TenantContext;
import com.kuspidartistmanagement.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final ArtistRepository artistRepository;
    private final EmailSendRepository emailSendRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReplyResponse> getUnprocessedReplies() {
        List<Reply> replies = replyRepository.findByProcessedFalse();
        return replies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReplyResponse> getArtistReplies(UUID artistId) {
        List<Reply> replies = replyRepository.findByArtistId(artistId);
        return replies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsProcessed(UUID replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found"));
        reply.markAsProcessed();
        replyRepository.save(reply);
        log.info("Reply marked as processed: {}", replyId);
    }

    @Override
    @Transactional
    public void processWebhookReply(Map<String, Object> payload) {
        try {
            String fromEmail = (String) payload.get("from");
            String subject = (String) payload.get("subject");
            String content = (String) payload.get("content");

            // Find artist by email
            UUID tenantId = getTenantId();
            Artist artist = artistRepository.findByEmailAndTenantId(fromEmail, tenantId)
                    .orElse(null);

            if (artist == null) {
                log.warn("Received reply from unknown email: {}", fromEmail);
                return;
            }

            // Try to find the original email send (optional)
            EmailSend emailSend = null;
            if (payload.containsKey("in_reply_to")) {
                String inReplyTo = (String) payload.get("in_reply_to");
                // You could store message IDs and look them up here
            }

            Reply reply = Reply.createReply(artist, emailSend, content, fromEmail, subject);
            replyRepository.save(reply);

            log.info("Reply recorded from artist: {} ({})", artist.getName(), fromEmail);
        } catch (Exception e) {
            log.error("Failed to process webhook reply", e);
        }
    }

    private UUID getTenantId() {
        UUID tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context not set");
        }
        return tenantId;
    }

    private ReplyResponse mapToResponse(Reply reply) {
        return ReplyResponse.builder()
                .id(reply.getId())
                .artistId(reply.getArtist().getId())
                .artistName(reply.getArtist().getName())
                .emailSendId(reply.getEmailSend() != null ? reply.getEmailSend().getId() : null)
                .content(reply.getContent())
                .fromEmail(reply.getFromEmail())
                .subject(reply.getSubject())
                .repliedAt(reply.getRepliedAt())
                .processed(reply.getProcessed())
                .build();
    }
}