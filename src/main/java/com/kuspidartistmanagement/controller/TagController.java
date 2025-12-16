package com.kuspidartistmanagement.controller;

import com.kuspidartistmanagement.dto.request.TagCreateRequest;
import com.kuspidartistmanagement.dto.response.ApiResponse;
import com.kuspidartistmanagement.dto.response.TagResponse;
import com.kuspidartistmanagement.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tags", description = "Tag management endpoints")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Create new tag")
    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> createTag(@Valid @RequestBody TagCreateRequest request) {
        TagResponse response = tagService.createTag(request);
        return ResponseEntity.ok(ApiResponse.success("Tag created successfully", response));
    }

    @Operation(summary = "Get all tags")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ResponseEntity.ok(ApiResponse.success(tags));
    }

    @Operation(summary = "Search tags by prefix")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TagResponse>>> searchTags(@RequestParam String prefix) {
        List<TagResponse> tags = tagService.searchTags(prefix);
        return ResponseEntity.ok(ApiResponse.success(tags));
    }

    @Operation(summary = "Delete tag")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(ApiResponse.success("Tag deleted successfully", null));
    }
}