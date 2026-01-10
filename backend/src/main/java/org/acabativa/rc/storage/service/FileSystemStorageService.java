package org.acabativa.rc.storage.service;

import org.acabativa.rc.storage.dto.FileListDTO;
import org.acabativa.rc.storage.entity.StoredFile;
import org.acabativa.rc.storage.entity.StoredFile.FileType;
import org.acabativa.rc.storage.entity.StoredFile.EntityType;
import org.acabativa.rc.storage.repository.StoredFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {
    
    private final StoredFileRepository repository;
    private final Path rootLocation;
    
    public FileSystemStorageService(StoredFileRepository repository, 
                                  @Value("${storage.location:/opt/claude/renovacampo/uploads}") String storageLocation) {
        this.repository = repository;
        this.rootLocation = Paths.get(storageLocation);
    }
    
    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(rootLocation.resolve("photos"));
            Files.createDirectories(rootLocation.resolve("documents"));
            Files.createDirectories(rootLocation.resolve("others"));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }
    
    @Override
    public StoredFile store(MultipartFile file, FileType fileType, Long entityId, EntityType entityType) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = generateFilename(entityId, fileExtension);
        
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + originalFilename);
            }
            
            Path destinationDir = getDestinationDirectory(fileType);
            Path destinationFile = destinationDir.resolve(newFilename);
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            StoredFile storedFile = new StoredFile(
                newFilename,
                originalFilename,
                destinationFile.toString(),
                file.getContentType(),
                file.getSize(),
                fileType,
                entityId,
                entityType
            );
            
            return repository.save(storedFile);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        }
    }
    
    @Override
    public Resource loadAsResource(Long fileId) {
        try {
            StoredFile file = repository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileId));
            
            Path filePath = Paths.get(file.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + fileId);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + fileId, e);
        }
    }
    
    @Override
    public StoredFile getFileInfo(Long fileId) {
        return repository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found: " + fileId));
    }
    
    @Override
    public void delete(Long fileId) {
        StoredFile file = repository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found: " + fileId));
        
        try {
            Path filePath = Paths.get(file.getFilePath());
            Files.deleteIfExists(filePath);
            repository.deleteById(fileId);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + fileId, e);
        }
    }
    
    @Override
    public StoredFile update(Long fileId, MultipartFile newFile) {
        StoredFile existingFile = repository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found: " + fileId));
        
        // Delete old file
        try {
            Path oldFilePath = Paths.get(existingFile.getFilePath());
            Files.deleteIfExists(oldFilePath);
        } catch (IOException e) {
            // Log error but continue
        }
        
        // Store new file
        String originalFilename = StringUtils.cleanPath(newFile.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = generateFilename(existingFile.getEntityId(), fileExtension);
        
        try {
            Path destinationDir = getDestinationDirectory(existingFile.getFileType());
            Path destinationFile = destinationDir.resolve(newFilename);
            
            try (InputStream inputStream = newFile.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            existingFile.setFileName(newFilename);
            existingFile.setOriginalFileName(originalFilename);
            existingFile.setFilePath(destinationFile.toString());
            existingFile.setContentType(newFile.getContentType());
            existingFile.setFileSize(newFile.getSize());
            existingFile.setUploadDate(LocalDateTime.now());
            
            return repository.save(existingFile);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to update file " + fileId, e);
        }
    }
    
    @Override
    public List<StoredFile> listByEntity(Long entityId, EntityType entityType) {
        return repository.findByEntityIdAndEntityType(entityId, entityType);
    }
    
    @Override
    public List<StoredFile> listByEntityAndType(Long entityId, EntityType entityType, FileType fileType) {
        return repository.findByEntityIdAndEntityTypeAndFileType(entityId, entityType, fileType);
    }
    
    @Override
    public List<FileListDTO> listBasicInfoByEntityAndType(Long entityId, EntityType entityType, FileType fileType) {
        List<Object[]> results = repository.findBasicInfoByEntityIdAndEntityTypeAndFileType(entityId, entityType, fileType);
        return results.stream().map(row -> {
            FileListDTO dto = new FileListDTO();
            dto.setId((Long) row[0]);
            dto.setOriginalFileName((String) row[1]);
            dto.setContentType((String) row[2]);
            dto.setFileSize((Long) row[3]);
            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }
    
    private Path getDestinationDirectory(FileType fileType) {
        return switch (fileType) {
            case PHOTO -> rootLocation.resolve("photos");
            case DOCUMENT -> rootLocation.resolve("documents");
            case OTHER -> rootLocation.resolve("others");
        };
    }
    
    private String generateFilename(Long entityId, String extension) {
        return String.format("%d_%s_%s%s", 
            entityId, 
            System.currentTimeMillis(),
            UUID.randomUUID().toString().substring(0, 8),
            extension);
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : filename.substring(lastDotIndex);
    }
}