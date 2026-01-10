package org.acabativa.rc.storage.dto;

import org.acabativa.rc.storage.entity.StoredFile;

public class FileListDTO {
    private Long id;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    
    public FileListDTO() {}
    
    public FileListDTO(StoredFile file) {
        this.id = file.getId();
        this.originalFileName = file.getOriginalFileName();
        this.contentType = file.getContentType();
        this.fileSize = file.getFileSize();
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
}