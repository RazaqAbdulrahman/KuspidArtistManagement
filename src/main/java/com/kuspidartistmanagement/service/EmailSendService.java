package com.kuspidartistmanagement.service;

import com.kuspidartistmanagement.dto.request.SendBeatRequest;
import com.kuspidartistmanagement.dto.response.SendJobResponse;

public interface EmailSendService {
    SendJobResponse sendBeatsToArtists(SendBeatRequest request);
}