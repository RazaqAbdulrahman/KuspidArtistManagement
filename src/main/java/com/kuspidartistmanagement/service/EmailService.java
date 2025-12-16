// EmailService.java (Interface)
package com.kuspidartistmanagement.service;

import com.kuspidartistmanagement.domain.entity.Artist;
import com.kuspidartistmanagement.domain.entity.Beat;
import com.kuspidartistmanagement.domain.entity.Pack;

import java.util.List;
import java.util.UUID;

public interface EmailService {
    UUID sendBeatsToArtists(List<Beat> beats, List<Artist> artists, String customSubject, String customMessage);
    UUID sendPackToArtists(Pack pack, List<Artist> artists, String customSubject, String customMessage);
    void processSendQueue();
    void retryFailedEmails();
}