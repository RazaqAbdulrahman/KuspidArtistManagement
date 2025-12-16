// PackService.java
package com.kuspidartistmanagement.service;

import com.kuspidartistmanagement.dto.request.PackCreateRequest;
import com.kuspidartistmanagement.dto.request.PackUpdateRequest;
import com.kuspidartistmanagement.dto.response.PackResponse;

import java.util.List;
import java.util.UUID;

public interface PackService {
    PackResponse createPack(PackCreateRequest request);
    PackResponse updatePack(UUID id, PackUpdateRequest request);
    PackResponse getPackById(UUID id);
    List<PackResponse> getAllPacks();
    PackResponse addBeatsToPack(UUID packId, List<UUID> beatIds);
    PackResponse removeBeatsFromPack(UUID packId, List<UUID> beatIds);
    void deletePack(UUID id);
}