package org.acabativa.rc.storage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stored_files")
public class StoredFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_sequence")
    @SequenceGenerator(name = "file_sequence", sequenceName = "file_sequence", allocationSize = 1)
    private Long id;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private String originalFileName;
    
    @Column(nullable = false)
    private String filePath;
    
    @Column(nullable = false)
    private String contentType;
    
    @Column(nullable = false)
    private Long fileSize;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;
    
    @Column(nullable = false)
    private LocalDateTime uploadDate;
    
    @Column(nullable = false)
    private Long entityId;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType entityType;
    
    @Column(columnDefinition = "TEXT")
    private String metadata;
    
    public enum FileType {
        PHOTO, DOCUMENT, OTHER
    }
    
    public enum EntityType {
        PROPERTY, PROJECT, INVESTOR, ENTERPRISE
    }
    
    public StoredFile() {
        this.uploadDate = LocalDateTime.now();
    }
    
    public StoredFile(String fileName, String originalFileName, String filePath, 
                      String contentType, Long fileSize, FileType fileType, Long entityId, EntityType entityType) {
        this();
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.entityId = entityId;
        this.entityType = entityType;
    }
    
    @Deprecated
    public StoredFile(String fileName, String originalFileName, String filePath, 
                      String contentType, Long fileSize, FileType fileType, Long propertyId) {
        this(fileName, originalFileName, filePath, contentType, fileSize, fileType, propertyId, EntityType.PROPERTY);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getOriginalFileName() {
        return originalFileName;
    }
    
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public FileType getFileType() {
        return fileType;
    }
    
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @Deprecated
    public Long getPropertyId() {
        return entityType == EntityType.PROPERTY ? entityId : null;
    }

    @Deprecated
    public void setPropertyId(Long propertyId) {
        this.entityId = propertyId;
        this.entityType = EntityType.PROPERTY;
    }
    
    public String getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}