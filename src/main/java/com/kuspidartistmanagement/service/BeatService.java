// BeatService.java
package com.kuspidartistmanagement.service;

import com.kuspidartistmanagement.dto.request.BeatCreateRequest;
import com.kuspidartistmanagement.dto.request.BeatUpdateRequest;
import com.kuspidartistmanagement.dto.response.BeatResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BeatService {
    BeatResponse createBeat(BeatCreateRequest request, MultipartFile file);
    BeatResponse updateBeat(UUID id, BeatUpdateRequest request);
    BeatResponse getBeatById(UUID id);
    List<BeatResponse> getAllBeats();
    List<BeatResponse> getBeatsByGenre(String genre);
    void deleteBeat(UUID id);
    String getBeatDownloadUrl(UUID id);
}