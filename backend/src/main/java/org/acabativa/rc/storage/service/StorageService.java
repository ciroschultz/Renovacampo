package org.acabativa.rc.storage.service;

import org.acabativa.rc.storage.dto.FileListDTO;
import org.acabativa.rc.storage.entity.StoredFile;
import org.acabativa.rc.storage.entity.StoredFile.FileType;
import org.acabativa.rc.storage.entity.StoredFile.EntityType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    
    // New generic methods for any entity type
    StoredFile store(MultipartFile file, FileType fileType, Long entityId, EntityType entityType);
    
    List<StoredFile> listByEntity(Long entityId, EntityType entityType);
    
    List<StoredFile> listByEntityAndType(Long entityId, EntityType entityType, FileType fileType);
    
    List<FileListDTO> listBasicInfoByEntityAndType(Long entityId, EntityType entityType, FileType fileType);
    
    // Backward compatibility methods for Property
    @Deprecated
    default StoredFile store(MultipartFile file, FileType fileType, Long propertyId) {
        return store(file, fileType, propertyId, EntityType.PROPERTY);
    }
    
    @Deprecated
    default List<StoredFile> listByProperty(Long propertyId) {
        return listByEntity(propertyId, EntityType.PROPERTY);
    }
    
    @Deprecated
    default List<StoredFile> listByPropertyAndType(Long propertyId, FileType fileType) {
        return listByEntityAndType(propertyId, EntityType.PROPERTY, fileType);
    }
    
    @Deprecated
    default List<FileListDTO> listBasicInfoByPropertyAndType(Long propertyId, FileType fileType) {
        return listBasicInfoByEntityAndType(propertyId, EntityType.PROPERTY, fileType);
    }
    
    // New convenience methods for Project
    default StoredFile storeForProject(MultipartFile file, FileType fileType, Long projectId) {
        return store(file, fileType, projectId, EntityType.PROJECT);
    }
    
    default List<StoredFile> listByProject(Long projectId) {
        return listByEntity(projectId, EntityType.PROJECT);
    }
    
    default List<StoredFile> listByProjectAndType(Long projectId, FileType fileType) {
        return listByEntityAndType(projectId, EntityType.PROJECT, fileType);
    }
    
    default List<FileListDTO> listBasicInfoByProjectAndType(Long projectId, FileType fileType) {
        return listBasicInfoByEntityAndType(projectId, EntityType.PROJECT, fileType);
    }

    // Convenience methods for Investor
    default StoredFile storeForInvestor(MultipartFile file, FileType fileType, Long investorId) {
        return store(file, fileType, investorId, EntityType.INVESTOR);
    }

    default List<StoredFile> listByInvestor(Long investorId) {
        return listByEntity(investorId, EntityType.INVESTOR);
    }

    default List<StoredFile> listByInvestorAndType(Long investorId, FileType fileType) {
        return listByEntityAndType(investorId, EntityType.INVESTOR, fileType);
    }

    default List<FileListDTO> listBasicInfoByInvestorAndType(Long investorId, FileType fileType) {
        return listBasicInfoByEntityAndType(investorId, EntityType.INVESTOR, fileType);
    }

    // Convenience methods for Enterprise
    default StoredFile storeForEnterprise(MultipartFile file, FileType fileType, Long enterpriseId) {
        return store(file, fileType, enterpriseId, EntityType.ENTERPRISE);
    }

    default List<StoredFile> listByEnterprise(Long enterpriseId) {
        return listByEntity(enterpriseId, EntityType.ENTERPRISE);
    }

    default List<StoredFile> listByEnterpriseAndType(Long enterpriseId, FileType fileType) {
        return listByEntityAndType(enterpriseId, EntityType.ENTERPRISE, fileType);
    }

    default List<FileListDTO> listBasicInfoByEnterpriseAndType(Long enterpriseId, FileType fileType) {
        return listBasicInfoByEntityAndType(enterpriseId, EntityType.ENTERPRISE, fileType);
    }

    // Common methods (unchanged)
    Resource loadAsResource(Long fileId);
    
    StoredFile getFileInfo(Long fileId);
    
    void delete(Long fileId);
    
    StoredFile update(Long fileId, MultipartFile file);
    
    void init();
}