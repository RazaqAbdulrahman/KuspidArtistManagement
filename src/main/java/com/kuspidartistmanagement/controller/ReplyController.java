package com.kuspidartistmanagement.controller;

import com.kuspidartistmanagement.dto.response.ApiResponse;
import com.kuspidartistmanagement.dto.response.ReplyResponse;
import com.kuspidartistmanagement.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Replies", description = "Artist reply management endpoints")
@RestController
@RequestMapping("/api/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "Get all unprocessed replies")
    @GetMapping("/unprocessed")
    public ResponseEntity<ApiResponse<List<ReplyResponse>>> getUnprocessedReplies() {
        List<ReplyResponse> replies = replyService.getUnprocessedReplies();
        return ResponseEntity.ok(ApiResponse.success(replies));
    }

    @Operation(summary = "Get replies for an artist")
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<ApiResponse<List<ReplyResponse>>> getArtistReplies(@PathVariable UUID artistId) {
        List<ReplyResponse> replies = replyService.getArtistReplies(artistId);
        return ResponseEntity.ok(ApiResponse.success(replies));
    }

    @Operation(summary = "Mark reply as processed")
    @PutMapping("/{id}/process")
    public ResponseEntity<ApiResponse<Void>> markAsProcessed(@PathVariable UUID id) {
        replyService.markAsProcessed(id);
        return ResponseEntity.ok(ApiResponse.success("Reply marked as processed", null));
    }
}