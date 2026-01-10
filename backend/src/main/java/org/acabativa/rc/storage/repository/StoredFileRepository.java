package org.acabativa.rc.storage.repository;

import org.acabativa.rc.storage.entity.StoredFile;
import org.acabativa.rc.storage.entity.StoredFile.FileType;
import org.acabativa.rc.storage.entity.StoredFile.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {
    
    List<StoredFile> findByEntityIdAndEntityType(Long entityId, EntityType entityType);
    
    List<StoredFile> findByEntityIdAndEntityTypeAndFileType(Long entityId, EntityType entityType, FileType fileType);
    
    List<StoredFile> findByFileType(FileType fileType);
    
    List<StoredFile> findByEntityType(EntityType entityType);
    
    @Query("SELECT s.id, s.originalFileName, s.contentType, s.fileSize FROM StoredFile s WHERE s.entityId = :entityId AND s.entityType = :entityType AND s.fileType = :fileType ORDER BY s.uploadDate DESC")
    List<Object[]> findBasicInfoByEntityIdAndEntityTypeAndFileType(@Param("entityId") Long entityId, @Param("entityType") EntityType entityType, @Param("fileType") FileType fileType);
    
    // Backward compatibility methods for Property
    @Deprecated
    default List<StoredFile> findByPropertyId(Long propertyId) {
        return findByEntityIdAndEntityType(propertyId, EntityType.PROPERTY);
    }
    
    @Deprecated
    default List<StoredFile> findByPropertyIdAndFileType(Long propertyId, FileType fileType) {
        return findByEntityIdAndEntityTypeAndFileType(propertyId, EntityType.PROPERTY, fileType);
    }
    
    @Deprecated
    default List<Object[]> findBasicInfoByPropertyIdAndFileType(Long propertyId, FileType fileType) {
        return findBasicInfoByEntityIdAndEntityTypeAndFileType(propertyId, EntityType.PROPERTY, fileType);
    }
    
    // New methods for Project
    default List<StoredFile> findByProjectId(Long projectId) {
        return findByEntityIdAndEntityType(projectId, EntityType.PROJECT);
    }
    
    default List<StoredFile> findByProjectIdAndFileType(Long projectId, FileType fileType) {
        return findByEntityIdAndEntityTypeAndFileType(projectId, EntityType.PROJECT, fileType);
    }
    
    default List<Object[]> findBasicInfoByProjectIdAndFileType(Long projectId, FileType fileType) {
        return findBasicInfoByEntityIdAndEntityTypeAndFileType(projectId, EntityType.PROJECT, fileType);
    }
}