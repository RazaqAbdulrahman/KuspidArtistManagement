package com.kuspidartistmanagement.controller;

import com.kuspidartistmanagement.dto.response.ApiResponse;
import com.kuspidartistmanagement.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Tag(name = "Webhooks", description = "Webhook endpoints for external integrations")
@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final ReplyService replyService;

    @Operation(summary = "Receive email reply webhook")
    @PostMapping("/email-reply")
    public ResponseEntity<ApiResponse<Void>> handleEmailReply(@RequestBody Map<String, Object> payload) {
        log.info("Received email reply webhook: {}", payload);
        replyService.processWebhookReply(payload);
        return ResponseEntity.ok(ApiResponse.success("Webhook processed", null));
    }
}