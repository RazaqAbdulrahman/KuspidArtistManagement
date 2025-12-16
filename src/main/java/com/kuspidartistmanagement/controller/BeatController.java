package com.kuspidartistmanagement.controller;

import com.kuspidartistmanagement.dto.request.BeatCreateRequest;
import com.kuspidartistmanagement.dto.request.BeatUpdateRequest;
import com.kuspidartistmanagement.dto.response.ApiResponse;
import com.kuspidartistmanagement.dto.response.BeatResponse;
import com.kuspidartistmanagement.service.BeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Beats", description = "Beat management endpoints")
@RestController
@RequestMapping("/api/beats")
@RequiredArgsConstructor
public class BeatController {

    private final BeatService beatService;

    @Operation(summary = "Upload and create new beat")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<BeatResponse>> createBeat(
            @Valid @ModelAttribute BeatCreateRequest request,
            @RequestParam("file") MultipartFile file) {
        BeatResponse response = beatService.createBeat(request, file);
        return ResponseEntity.ok(ApiResponse.success("Beat created successfully", response));
    }

    @Operation(summary = "Update beat metadata")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BeatResponse>> updateBeat(
            @PathVariable UUID id,
            @Valid @RequestBody BeatUpdateRequest request) {
        BeatResponse response = beatService.updateBeat(id, request);
        return ResponseEntity.ok(ApiResponse.success("Beat updated successfully", response));
    }

    @Operation(summary = "Get beat by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BeatResponse>> getBeat(@PathVariable UUID id) {
        BeatResponse response = beatService.getBeatById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all beats")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BeatResponse>>> getAllBeats() {
        List<BeatResponse> beats = beatService.getAllBeats();
        return ResponseEntity.ok(ApiResponse.success(beats));
    }

    @Operation(summary = "Get beats by genre")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<ApiResponse<List<BeatResponse>>> getBeatsByGenre(@PathVariable String genre) {
        List<BeatResponse> beats = beatService.getBeatsByGenre(genre);
        return ResponseEntity.ok(ApiResponse.success(beats));
    }

    @Operation(summary = "Get beat download URL")
    @GetMapping("/{id}/download-url")
    public ResponseEntity<ApiResponse<String>> getBeatDownloadUrl(@PathVariable UUID id) {
        String url = beatService.getBeatDownloadUrl(id);
        return ResponseEntity.ok(ApiResponse.success(url));
    }

    @Operation(summary = "Delete beat")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBeat(@PathVariable UUID id) {
        beatService.deleteBeat(id);
        return ResponseEntity.ok(ApiResponse.success("Beat deleted successfully", null));
    }
}