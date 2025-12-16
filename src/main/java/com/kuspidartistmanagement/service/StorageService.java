// StorageService.java (Interface)
package com.kuspidartistmanagement.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StorageService {
    String store(MultipartFile file, UUID tenantId, String category);
    byte[] load(String fileUrl);
    void delete(String fileUrl);
    String getPublicUrl(String fileUrl);
}