package com.kuspidartistmanagement.controller;

import com.kuspidartistmanagement.dto.request.SendBeatRequest;
import com.kuspidartistmanagement.dto.response.ApiResponse;
import com.kuspidartistmanagement.dto.response.SendJobResponse;
import com.kuspidartistmanagement.service.EmailSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Email", description = "Email sending endpoints")
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailSendService emailSendService;

    @Operation(summary = "Send beats/pack to filtered artists")
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<SendJobResponse>> sendBeats(@Valid @RequestBody SendBeatRequest request) {
        SendJobResponse response = emailSendService.sendBeatsToArtists(request);
        return ResponseEntity.ok(ApiResponse.success("Emails queued successfully", response));
    }
}