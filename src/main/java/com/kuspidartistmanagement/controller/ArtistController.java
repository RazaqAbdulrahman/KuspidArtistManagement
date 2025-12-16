package com.kuspidartistmanagement.controller;

import com.kuspidartistmanagement.dto.request.ArtistCreateRequest;
import com.kuspidartistmanagement.dto.request.ArtistFilterRequest;
import com.kuspidartistmanagement.dto.request.ArtistUpdateRequest;
import com.kuspidartistmanagement.dto.response.ApiResponse;
import com.kuspidartistmanagement.dto.response.ArtistResponse;
import com.kuspidartistmanagement.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Artists", description = "Artist management endpoints")
@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @Operation(summary = "Create new artist")
    @PostMapping
    public ResponseEntity<ApiResponse<ArtistResponse>> createArtist(@Valid @RequestBody ArtistCreateRequest request) {
        ArtistResponse response = artistService.createArtist(request);
        return ResponseEntity.ok(ApiResponse.success("Artist created successfully", response));
    }

    @Operation(summary = "Update artist")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ArtistResponse>> updateArtist(
            @PathVariable UUID id,
            @Valid @RequestBody ArtistUpdateRequest request) {
        ArtistResponse response = artistService.updateArtist(id, request);
        return ResponseEntity.ok(ApiResponse.success("Artist updated successfully", response));
    }

    @Operation(summary = "Get artist by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ArtistResponse>> getArtist(@PathVariable UUID id) {
        ArtistResponse response = artistService.getArtistById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all artists")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ArtistResponse>>> getAllArtists() {
        List<ArtistResponse> artists = artistService.getAllArtists();
        return ResponseEntity.ok(ApiResponse.success(artists));
    }

    @Operation(summary = "Filter artists by criteria")
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<List<ArtistResponse>>> filterArtists(@RequestBody ArtistFilterRequest filter) {
        List<ArtistResponse> artists = artistService.filterArtists(filter);
        return ResponseEntity.ok(ApiResponse.success(artists));
    }

    @Operation(summary = "Delete artist (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArtist(@PathVariable UUID id) {
        artistService.deleteArtist(id);
        return ResponseEntity.ok(ApiResponse.success("Artist deleted successfully", null));
    }
}