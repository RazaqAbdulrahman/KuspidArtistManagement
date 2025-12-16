package com.kuspidartistmanagement.service;

import com.kuspidartistmanagement.dto.request.TagCreateRequest;
import com.kuspidartistmanagement.dto.response.TagResponse;

import java.util.List;
import java.util.UUID;

public interface TagService {
    TagResponse createTag(TagCreateRequest request);
    List<TagResponse> getAllTags();
    List<TagResponse> searchTags(String prefix);
    void deleteTag(UUID id);
}