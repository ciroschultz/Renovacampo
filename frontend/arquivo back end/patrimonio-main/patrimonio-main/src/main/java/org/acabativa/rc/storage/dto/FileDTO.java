package org.acabativa.rc.storage.dto;

import org.acabativa.rc.storage.entity.StoredFile;
import java.time.LocalDateTime;

public class FileDTO {
    private Long id;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadDate;
    private Long propertyId;
    
    public FileDTO() {}
    
    public FileDTO(StoredFile file) {
        this.id = file.getId();
        this.originalFileName = file.getOriginalFileName();
        this.contentType = file.getContentType();
        this.fileSize = file.getFileSize();
        this.fileType = file.getFileType().toString();
        this.uploadDate = file.getUploadDate();
        this.propertyId = file.getPropertyId();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalFileName() {
        return originalFileName;
    }
    
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
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
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    public Long getPropertyId() {
        return propertyId;
    }
    
    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}