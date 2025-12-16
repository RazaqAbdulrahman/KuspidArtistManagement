package com.kuspidartistmanagement.service;

import com.kuspidartistmanagement.dto.response.ReplyResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReplyService {
    List<ReplyResponse> getUnprocessedReplies();
    List<ReplyResponse> getArtistReplies(UUID artistId);
    void markAsProcessed(UUID replyId);
    void processWebhookReply(Map<String, Object> payload);
}