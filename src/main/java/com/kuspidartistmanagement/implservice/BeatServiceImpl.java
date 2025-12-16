package com.kuspidartistmanagement.implservice;

import com.kuspidartistmanagement.domain.entity.Beat;
import com.kuspidartistmanagement.domain.entity.Tenant;
import com.kuspidartistmanagement.dto.request.BeatCreateRequest;
import com.kuspidartistmanagement.dto.request.BeatUpdateRequest;
import com.kuspidartistmanagement.dto.response.BeatResponse;
import com.kuspidartistmanagement.exception.ResourceNotFoundException;
import com.kuspidartistmanagement.repository.BeatRepository;
import com.kuspidartistmanagement.repository.TenantRepository;
import com.kuspidartistmanagement.security.TenantContext;
import com.kuspidartistmanagement.service.BeatService;
import com.kuspidartistmanagement.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeatServiceImpl implements BeatService {

    private final BeatRepository beatRepository;
    private final TenantRepository tenantRepository;
    private final StorageService storageService;

    @Override
    @Transactional
    public BeatResponse createBeat(BeatCreateRequest request, MultipartFile file) {
        UUID tenantId = getTenantId();
        log.debug("Creating beat for tenant: {}", tenantId);

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Beat file is required");
        }

        Tenant tenant = getTenant(tenantId);

        // Upload file to storage
        String fileUrl = storageService.store(file, tenantId, "beats");

        Beat beat = Beat.builder()
                .title(request.getTitle())
                .bpm(request.getBpm())
                .genre(request.getGenre())
                .mood(request.getMood())
                .fileUrl(fileUrl)
                .fileSize(file.getSize())
                .tenant(tenant)
                .active(true)
                .build();

        beat = beatRepository.save(beat);
        log.info("Beat created: {} (ID: {})", beat.getTitle(), beat.getId());

        return mapToResponse(beat);
    }

    @Override
    @Transactional
    public BeatResponse updateBeat(UUID id, BeatUpdateRequest request) {
        UUID tenantId = getTenantId();
        Beat beat = getBeatEntity(id, tenantId);

        if (request.getTitle() != null) {
            beat.setTitle(request.getTitle());
        }
        if (request.getBpm() != null) {
            beat.setBpm(request.getBpm());
        }
        if (request.getGenre() != null) {
            beat.setGenre(request.getGenre());
        }
        if (request.getMood() != null) {
            beat.setMood(request.getMood());
        }
        if (request.getActive() != null) {
            beat.setActive(request.getActive());
        }

        beat = beatRepository.save(beat);
        log.info("Beat updated: {} (ID: {})", beat.getTitle(), beat.getId());

        return mapToResponse(beat);
    }

    @Override
    @Transactional(readOnly = true)
    public BeatResponse getBeatById(UUID id) {
        UUID tenantId = getTenantId();
        Beat beat = getBeatEntity(id, tenantId);
        return mapToResponse(beat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeatResponse> getAllBeats() {
        UUID tenantId = getTenantId();
        List<Beat> beats = beatRepository.findByTenantIdAndActiveTrue(tenantId);
        return beats.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeatResponse> getBeatsByGenre(String genre) {
        UUID tenantId = getTenantId();
        List<Beat> beats = beatRepository.findByTenantIdAndGenre(tenantId, genre);
        return beats.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBeat(UUID id) {
        UUID tenantId = getTenantId();
        Beat beat = getBeatEntity(id, tenantId);

        // Soft delete
        beat.setActive(false);
        beatRepository.save(beat);
        log.info("Beat soft-deleted: {} (ID: {})", beat.getTitle(), beat.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public String getBeatDownloadUrl(UUID id) {
        UUID tenantId = getTenantId();
        Beat beat = getBeatEntity(id, tenantId);
        return storageService.getPublicUrl(beat.getFileUrl());
    }

    private Beat getBeatEntity(UUID id, UUID tenantId) {
        return beatRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Beat not found"));
    }

    private Tenant getTenant(UUID tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
    }

    private UUID getTenantId() {
        UUID tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context not set");
        }
        return tenantId;
    }

    private BeatResponse mapToResponse(Beat beat) {
        return BeatResponse.builder()
                .id(beat.getId())
                .title(beat.getTitle())
                .bpm(beat.getBpm())
                .genre(beat.getGenre())
                .mood(beat.getMood())
                .fileUrl(beat.getFileUrl())
                .fileSize(beat.getFileSize())
                .durationSeconds(beat.getDurationSeconds())
                .active(beat.getActive())
                .createdAt(beat.getCreatedAt())
                .build();
    }
}