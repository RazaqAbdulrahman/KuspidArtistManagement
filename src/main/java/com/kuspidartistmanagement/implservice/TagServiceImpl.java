package com.kuspidartistmanagement.implservice;

import com.kuspidartistmanagement.domain.entity.Tag;
import com.kuspidartistmanagement.domain.entity.Tenant;
import com.kuspidartistmanagement.dto.request.TagCreateRequest;
import com.kuspidartistmanagement.dto.response.TagResponse;
import com.kuspidartistmanagement.exception.ResourceNotFoundException;
import com.kuspidartistmanagement.repository.TagRepository;
import com.kuspidartistmanagement.repository.TenantRepository;
import com.kuspidartistmanagement.security.TenantContext;
import com.kuspidartistmanagement.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public TagResponse createTag(TagCreateRequest request) {
        UUID tenantId = getTenantId();
        String normalizedName = request.getName().toLowerCase().trim();

        // Check if tag already exists
        if (tagRepository.existsByNameAndTenantId(normalizedName, tenantId)) {
            throw new IllegalArgumentException("Tag with this name already exists");
        }

        Tenant tenant = getTenant(tenantId);

        Tag tag = Tag.createTag(normalizedName, request.getType(), tenant);
        tag = tagRepository.save(tag);

        log.info("Tag created: {} (ID: {})", tag.getName(), tag.getId());

        return mapToResponse(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        UUID tenantId = getTenantId();
        List<Tag> tags = tagRepository.findByTenantId(tenantId);
        return tags.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> searchTags(String prefix) {
        UUID tenantId = getTenantId();
        List<Tag> tags = tagRepository.findByNameStartingWithIgnoreCase(tenantId, prefix);
        return tags.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTag(UUID id) {
        UUID tenantId = getTenantId();
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        if (!tag.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Tag does not belong to current tenant");
        }

        tagRepository.delete(tag);
        log.info("Tag deleted: {} (ID: {})", tag.getName(), tag.getId());
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

    private TagResponse mapToResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .type(tag.getType())
                .build();
    }
}