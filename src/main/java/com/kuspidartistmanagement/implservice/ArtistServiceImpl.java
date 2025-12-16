package com.kuspidartistmanagement.implservice;

import com.kuspidartistmanagement.domain.entity.Artist;
import com.kuspidartistmanagement.domain.entity.Tag;
import com.kuspidartistmanagement.domain.entity.Tenant;
import com.kuspidartistmanagement.dto.request.ArtistCreateRequest;
import com.kuspidartistmanagement.dto.request.ArtistFilterRequest;
import com.kuspidartistmanagement.dto.request.ArtistUpdateRequest;
import com.kuspidartistmanagement.dto.response.ArtistResponse;
import com.kuspidartistmanagement.dto.response.TagResponse;
import com.kuspidartistmanagement.exception.ResourceNotFoundException;
import com.kuspidartistmanagement.repository.ArtistRepository;
import com.kuspidartistmanagement.repository.TagRepository;
import com.kuspidartistmanagement.repository.TenantRepository;
import com.kuspidartistmanagement.security.TenantContext;
import com.kuspidartistmanagement.service.ArtistService;
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
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final TagRepository tagRepository;
    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public ArtistResponse createArtist(ArtistCreateRequest request) {
        UUID tenantId = getTenantId();
        log.debug("Creating artist for tenant: {}", tenantId);

        // Check if artist with email already exists
        if (artistRepository.existsByEmailAndTenantId(request.getEmail(), tenantId)) {
            throw new IllegalArgumentException("Artist with this email already exists");
        }

        Tenant tenant = getTenant(tenantId);

        Artist artist = Artist.builder()
                .name(request.getName())
                .email(request.getEmail())
                .genre(request.getGenre())
                .priorityLevel(request.getPriorityLevel())
                .tenant(tenant)
                .active(true)
                .build();

        // Add tags if provided
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            tags.forEach(artist::addTag);
        }

        artist = artistRepository.save(artist);
        log.info("Artist created: {} (ID: {})", artist.getName(), artist.getId());

        return mapToResponse(artist);
    }

    @Override
    @Transactional
    public ArtistResponse updateArtist(UUID id, ArtistUpdateRequest request) {
        UUID tenantId = getTenantId();
        Artist artist = getArtistEntity(id, tenantId);

        if (request.getName() != null) {
            artist.setName(request.getName());
        }
        if (request.getEmail() != null) {
            // Check email uniqueness
            if (!artist.getEmail().equals(request.getEmail()) &&
                    artistRepository.existsByEmailAndTenantId(request.getEmail(), tenantId)) {
                throw new IllegalArgumentException("Artist with this email already exists");
            }
            artist.setEmail(request.getEmail());
        }
        if (request.getGenre() != null) {
            artist.setGenre(request.getGenre());
        }
        if (request.getPriorityLevel() != null) {
            artist.updatePriority(request.getPriorityLevel());
        }
        if (request.getActive() != null) {
            artist.setActive(request.getActive());
        }

        // Update tags if provided
        if (request.getTagIds() != null) {
            artist.getArtistTags().clear();
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            tags.forEach(artist::addTag);
        }

        artist = artistRepository.save(artist);
        log.info("Artist updated: {} (ID: {})", artist.getName(), artist.getId());

        return mapToResponse(artist);
    }

    @Override
    @Transactional(readOnly = true)
    public ArtistResponse getArtistById(UUID id) {
        UUID tenantId = getTenantId();
        Artist artist = getArtistEntity(id, tenantId);
        return mapToResponse(artist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistResponse> getAllArtists() {
        UUID tenantId = getTenantId();
        List<Artist> artists = artistRepository.findByTenantIdAndActiveTrue(tenantId);
        return artists.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistResponse> filterArtists(ArtistFilterRequest filter) {
        UUID tenantId = getTenantId();

        // If specific artist IDs provided, use those
        if (filter.getSpecificArtistIds() != null && !filter.getSpecificArtistIds().isEmpty()) {
            List<Artist> artists = artistRepository.findAllById(filter.getSpecificArtistIds());
            return artists.stream()
                    .filter(a -> a.getTenantId().equals(tenantId) && a.getActive())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }

        // Handle tag filtering with AND/OR logic
        if (filter.getTagIds() != null && !filter.getTagIds().isEmpty()) {
            List<Artist> artists;
            if (filter.getTagMatchMode() == ArtistFilterRequest.TagMatchMode.AND) {
                artists = artistRepository.findByTenantIdAndAllTags(
                        tenantId,
                        filter.getTagIds(),
                        filter.getTagIds().size()
                );
            } else {
                artists = artistRepository.findByTenantIdAndAnyTags(tenantId, filter.getTagIds());
            }

            // Apply additional filters
            return artists.stream()
                    .filter(a -> filter.getGenre() == null ||
                            a.getGenre().equals(filter.getGenre()))
                    .filter(a -> filter.getPriorityLevel() == null ||
                            a.getPriorityLevel() == filter.getPriorityLevel())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }

        // Use complex filter query
        List<Artist> artists = artistRepository.findByComplexFilter(
                tenantId,
                filter.getGenre(),
                filter.getPriorityLevel(),
                filter.getTagIds()
        );

        return artists.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteArtist(UUID id) {
        UUID tenantId = getTenantId();
        Artist artist = getArtistEntity(id, tenantId);
        artist.setActive(false);
        artistRepository.save(artist);
        log.info("Artist soft-deleted: {} (ID: {})", artist.getName(), artist.getId());
    }

    private Artist getArtistEntity(UUID id, UUID tenantId) {
        return artistRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
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

    private ArtistResponse mapToResponse(Artist artist) {
        Set<TagResponse> tagResponses = artist.getArtistTags().stream()
                .map(at -> TagResponse.builder()
                        .id(at.getTag().getId())
                        .name(at.getTag().getName())
                        .type(at.getTag().getType())
                        .build())
                .collect(Collectors.toSet());

        return ArtistResponse.builder()
                .id(artist.getId())
                .name(artist.getName())
                .email(artist.getEmail())
                .genre(artist.getGenre())
                .priorityLevel(artist.getPriorityLevel())
                .tags(tagResponses)
                .active(artist.getActive())
                .createdAt(artist.getCreatedAt())
                .updatedAt(artist.getUpdatedAt())
                .build();
    }
}