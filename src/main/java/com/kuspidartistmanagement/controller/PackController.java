package com.kuspidartistmanagement.controller;

import com.kuspidartistmanagement.dto.request.PackCreateRequest;
import com.kuspidartistmanagement.dto.request.PackUpdateRequest;
import com.kuspidartistmanagement.dto.response.ApiResponse;
import com.kuspidartistmanagement.dto.response.PackResponse;
import com.kuspidartistmanagement.service.PackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Packs", description = "Beat pack management endpoints")
@RestController
@RequestMapping("/api/packs")
@RequiredArgsConstructor
public class PackController {

    private final PackService packService;

    @Operation(summary = "Create new pack")
    @PostMapping
    public ResponseEntity<ApiResponse<PackResponse>> createPack(@Valid @RequestBody PackCreateRequest request) {
        PackResponse response = packService.createPack(request);
        return ResponseEntity.ok(ApiResponse.success("Pack created successfully", response));
    }

    @Operation(summary = "Update pack")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PackResponse>> updatePack(
            @PathVariable UUID id,
            @Valid @RequestBody PackUpdateRequest request) {
        PackResponse response = packService.updatePack(id, request);
        return ResponseEntity.ok(ApiResponse.success("Pack updated successfully", response));
    }

    @Operation(summary = "Get pack by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PackResponse>> getPack(@PathVariable UUID id) {
        PackResponse response = packService.getPackById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all packs")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PackResponse>>> getAllPacks() {
        List<PackResponse> packs = packService.getAllPacks();
        return ResponseEntity.ok(ApiResponse.success(packs));
    }

    @Operation(summary = "Add beats to pack")
    @PostMapping("/{id}/beats")
    public ResponseEntity<ApiResponse<PackResponse>> addBeats(
            @PathVariable UUID id,
            @RequestBody List<UUID> beatIds) {
        PackResponse response = packService.addBeatsToPack(id, beatIds);
        return ResponseEntity.ok(ApiResponse.success("Beats added to pack", response));
    }

    @Operation(summary = "Remove beats from pack")
    @DeleteMapping("/{id}/beats")
    public ResponseEntity<ApiResponse<PackResponse>> removeBeats(
            @PathVariable UUID id,
            @RequestBody List<UUID> beatIds) {
        PackResponse response = packService.removeBeatsFromPack(id, beatIds);
        return ResponseEntity.ok(ApiResponse.success("Beats removed from pack", response));
    }

    @Operation(summary = "Delete pack")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePack(@PathVariable UUID id) {
        packService.deletePack(id);
        return ResponseEntity.ok(ApiResponse.success("Pack deleted successfully", null));
    }
}