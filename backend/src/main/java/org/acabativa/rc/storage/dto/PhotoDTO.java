package org.acabativa.rc.storage.dto;

import org.acabativa.rc.storage.entity.StoredFile;

public class PhotoDTO extends FileDTO {
    private String thumbnailUrl;
    private String fullSizeUrl;
    private Integer width;
    private Integer height;
    
    public PhotoDTO() {
        super();
    }
    
    public PhotoDTO(StoredFile file) {
        super(file);
        this.fullSizeUrl = "/api/v1/photos/" + file.getId();
        this.thumbnailUrl = "/api/v1/photos/" + file.getId() + "/thumbnail";
    }
    
    // Getters and Setters
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    
    public String getFullSizeUrl() {
        return fullSizeUrl;
    }
    
    public void setFullSizeUrl(String fullSizeUrl) {
        this.fullSizeUrl = fullSizeUrl;
    }
    
    public Integer getWidth() {
        return width;
    }
    
    public void setWidth(Integer width) {
        this.width = width;
    }
    
    public Integer getHeight() {
        return height;
    }
    
    public void setHeight(Integer height) {
        this.height = height;
    }
}