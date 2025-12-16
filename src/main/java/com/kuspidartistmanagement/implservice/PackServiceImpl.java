package com.kuspidartistmanagement.implservice;

import com.kuspidartistmanagement.domain.entity.Beat;
import com.kuspidartistmanagement.domain.entity.Pack;
import com.kuspidartistmanagement.domain.entity.Tenant;
import com.kuspidartistmanagement.dto.request.PackCreateRequest;
import com.kuspidartistmanagement.dto.request.PackUpdateRequest;
import com.kuspidartistmanagement.dto.response.BeatResponse;
import com.kuspidartistmanagement.dto.response.PackResponse;
import com.kuspidartistmanagement.exception.ResourceNotFoundException;
import com.kuspidartistmanagement.repository.BeatRepository;
import com.kuspidartistmanagement.repository.PackRepository;
import com.kuspidartistmanagement.repository.TenantRepository;
import com.kuspidartistmanagement.security.TenantContext;
import com.kuspidartistmanagement.service.PackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PackServiceImpl implements PackService {

    private final PackRepository packRepository;
    private final BeatRepository beatRepository;
    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public PackResponse createPack(PackCreateRequest request) {
        UUID tenantId = getTenantId();
        Tenant tenant = getTenant(tenantId);

        Pack pack = Pack.createPack(request.getName(), tenant);
        pack.setDescription(request.getDescription());

        if (request.getBeatIds() != null && !request.getBeatIds().isEmpty()) {
            List<Beat> beats = beatRepository.findAllById(request.getBeatIds());
            beats.forEach(pack::addBeat);
        }

        pack = packRepository.save(pack);
        log.info("Pack created: {} (ID: {})", pack.getName(), pack.getId());

        return mapToResponse(pack);
    }

    @Override
    @Transactional
    public PackResponse updatePack(UUID id, PackUpdateRequest request) {
        UUID tenantId = getTenantId();
        Pack pack = getPackEntity(id, tenantId);

        if (request.getName() != null && !request.getName().isBlank()) {
            pack.setName(request.getName());
        }
        if (request.getDescription() != null) {
            pack.setDescription(request.getDescription());
        }
        if (request.getActive() != null) {
            pack.setActive(request.getActive());
        }

        if (request.getBeatIds() != null) {
            pack.getPackBeats().clear();
            List<Beat> beats = beatRepository.findAllById(request.getBeatIds());
            beats.forEach(pack::addBeat);
        }

        pack = packRepository.save(pack);
        log.info("Pack updated: {} (ID: {})", pack.getName(), pack.getId());

        return mapToResponse(pack);
    }

    @Override
    @Transactional(readOnly = true)
    public PackResponse getPackById(UUID id) {
        UUID tenantId = getTenantId();
        Pack pack = getPackEntity(id, tenantId);
        return mapToResponse(pack);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackResponse> getAllPacks() {
        UUID tenantId = getTenantId();
        List<Pack> packs = packRepository.findByTenantIdAndActiveTrue(tenantId);
        return packs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PackResponse addBeatsToPack(UUID packId, List<UUID> beatIds) {
        UUID tenantId = getTenantId();
        Pack pack = getPackEntity(packId, tenantId);

        List<Beat> beats = beatRepository.findAllById(beatIds);
        beats.forEach(pack::addBeat);

        pack = packRepository.save(pack);
        log.info("Added {} beats to pack: {}", beatIds.size(), pack.getName());

        return mapToResponse(pack);
    }

    @Override
    @Transactional
    public PackResponse removeBeatsFromPack(UUID packId, List<UUID> beatIds) {
        UUID tenantId = getTenantId();
        Pack pack = getPackEntity(packId, tenantId);

        List<Beat> beats = beatRepository.findAllById(beatIds);
        beats.forEach(pack::removeBeat);

        pack = packRepository.save(pack);
        log.info("Removed {} beats from pack: {}", beatIds.size(), pack.getName());

        return mapToResponse(pack);
    }

    @Override
    @Transactional
    public void deletePack(UUID id) {
        UUID tenantId = getTenantId();
        Pack pack = getPackEntity(id, tenantId);

        pack.setActive(false);
        packRepository.save(pack);
        log.info("Pack soft-deleted: {} (ID: {})", pack.getName(), pack.getId());
    }

    private Pack getPackEntity(UUID id, UUID tenantId) {
        return packRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found"));
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

    private PackResponse mapToResponse(Pack pack) {
        Set<BeatResponse> beatResponses = pack.getPackBeats().stream()
                .map(pb -> BeatResponse.builder()
                        .id(pb.getBeat().getId())
                        .title(pb.getBeat().getTitle())
                        .bpm(pb.getBeat().getBpm())
                        .genre(pb.getBeat().getGenre())
                        .mood(pb.getBeat().getMood())
                        .fileUrl(pb.getBeat().getFileUrl())
                        .fileSize(pb.getBeat().getFileSize())
                        .active(pb.getBeat().getActive())
                        .createdAt(pb.getBeat().getCreatedAt())
                        .build())
                .collect(Collectors.toSet());

        return PackResponse.builder()
                .id(pack.getId())
                .name(pack.getName())
                .description(pack.getDescription())
                .beats(beatResponses)
                .beatCount(pack.getBeatCount())
                .active(pack.getActive())
                .createdAt(pack.getCreatedAt())
                .build();
    }
}