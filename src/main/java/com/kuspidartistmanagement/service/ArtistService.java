package com.kuspidartistmanagement.service;

import com.kuspidartistmanagement.dto.request.ArtistCreateRequest;
import com.kuspidartistmanagement.dto.request.ArtistFilterRequest;
import com.kuspidartistmanagement.dto.request.ArtistUpdateRequest;
import com.kuspidartistmanagement.dto.response.ArtistResponse;

import java.util.List;
import java.util.UUID;

public interface ArtistService {
    ArtistResponse createArtist(ArtistCreateRequest request);
    ArtistResponse updateArtist(UUID id, ArtistUpdateRequest request);
    ArtistResponse getArtistById(UUID id);
    List<ArtistResponse> getAllArtists();
    List<ArtistResponse> filterArtists(ArtistFilterRequest filter);
    void deleteArtist(UUID id);
}