// LocalStorageServiceImpl.java
package com.kuspidartistmanagement.implservice;

import com.kuspidartistmanagement.config.StorageConfig;
import com.kuspidartistmanagement.exception.StorageException;
import com.kuspidartistmanagement.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.storage.provider", havingValue = "local", matchIfMissing = true)
@RequiredArgsConstructor
public class LocalStorageServiceImpl implements StorageService {

    private final StorageConfig storageConfig;

    @Override
    public String store(MultipartFile file, UUID tenantId, String category) {
        try {
            String filename = generateUniqueFilename(file.getOriginalFilename());
            Path storagePath = getStoragePath(tenantId, category);

            // Create directories if they don't exist
            Files.createDirectories(storagePath);

            Path filePath = storagePath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File stored locally: {}", filePath);

            return String.format("%s/%s/%s", tenantId, category, filename);
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public byte[] load(String fileUrl) {
        try {
            Path filePath = Paths.get(storageConfig.getLocalPath(), fileUrl);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Failed to load file: {}", fileUrl, e);
            throw new StorageException("Failed to load file", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        try {
            Path filePath = Paths.get(storageConfig.getLocalPath(), fileUrl);
            Files.deleteIfExists(filePath);
            log.info("File deleted: {}", fileUrl);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileUrl, e);
            throw new StorageException("Failed to delete file", e);
        }
    }

    @Override
    public String getPublicUrl(String fileUrl) {
        // For local storage, return API endpoint to download
        return "/api/storage/download/" + fileUrl;
    }

    private Path getStoragePath(UUID tenantId, String category) {
        return Paths.get(storageConfig.getLocalPath(), tenantId.toString(), category);
    }

    private String generateUniqueFilename(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID() + extension;
    }
}