package org.acabativa.rc.storage.controller;

import org.acabativa.rc.storage.dto.FileDTO;
import org.acabativa.rc.storage.dto.FileListDTO;
import org.acabativa.rc.storage.entity.StoredFile;
import org.acabativa.rc.storage.entity.StoredFile.FileType;
import org.acabativa.rc.storage.entity.StoredFile.EntityType;
import org.acabativa.rc.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/files")
public class FileController {
    
    private final StorageService storageService;
    
    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @PostMapping("/upload/{propertyId}")
    @ResponseBody
    public ResponseEntity<FileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long propertyId,
            @RequestParam(value = "type", defaultValue = "DOCUMENT") String fileType) {
        
        FileType type = FileType.valueOf(fileType.toUpperCase());
        StoredFile storedFile = storageService.store(file, type, propertyId);
        return ResponseEntity.ok(new FileDTO(storedFile));
    }
    
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        StoredFile fileInfo = storageService.getFileInfo(id);
        Resource file = storageService.loadAsResource(id);

        String filename = fileInfo.getOriginalFileName();
        // Encode filename for Content-Disposition header (RFC 5987)
        String encodedFilename = java.net.URLEncoder.encode(filename, java.nio.charset.StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                       "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + encodedFilename)
                .body(file);
    }
    
    @GetMapping("/{id}/info")
    @ResponseBody
    public ResponseEntity<FileDTO> getFileInfo(@PathVariable Long id) {
        StoredFile file = storageService.getFileInfo(id);
        return ResponseEntity.ok(new FileDTO(file));
    }
    
    @GetMapping("/property/{propertyId}")
    @ResponseBody
    public ResponseEntity<List<FileDTO>> getFilesByProperty(@PathVariable Long propertyId) {
        List<FileDTO> files = storageService.listByProperty(propertyId)
                .stream()
                .map(FileDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }
    
    // New Project endpoints
    @PostMapping("/upload/project/{projectId}")
    @ResponseBody
    public ResponseEntity<FileDTO> uploadFileToProject(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long projectId,
            @RequestParam(value = "type", defaultValue = "DOCUMENT") String fileType) {
        
        FileType type = FileType.valueOf(fileType.toUpperCase());
        StoredFile storedFile = storageService.storeForProject(file, type, projectId);
        return ResponseEntity.ok(new FileDTO(storedFile));
    }
    
    @GetMapping("/project/{projectId}")
    @ResponseBody
    public ResponseEntity<List<FileDTO>> getFilesByProject(@PathVariable Long projectId) {
        List<FileDTO> files = storageService.listByProject(projectId)
                .stream()
                .map(FileDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }

    // Investor endpoints
    @PostMapping("/upload/investor/{investorId}")
    @ResponseBody
    public ResponseEntity<FileDTO> uploadFileToInvestor(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long investorId,
            @RequestParam(value = "type", defaultValue = "DOCUMENT") String fileType) {

        FileType type = FileType.valueOf(fileType.toUpperCase());
        StoredFile storedFile = storageService.storeForInvestor(file, type, investorId);
        return ResponseEntity.ok(new FileDTO(storedFile));
    }

    @GetMapping("/investor/{investorId}")
    @ResponseBody
    public ResponseEntity<List<FileDTO>> getFilesByInvestor(@PathVariable Long investorId) {
        List<FileDTO> files = storageService.listByInvestor(investorId)
                .stream()
                .map(FileDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }

    // Enterprise endpoints
    @PostMapping("/upload/enterprise/{enterpriseId}")
    @ResponseBody
    public ResponseEntity<FileDTO> uploadFileToEnterprise(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long enterpriseId,
            @RequestParam(value = "type", defaultValue = "DOCUMENT") String fileType) {

        FileType type = FileType.valueOf(fileType.toUpperCase());
        StoredFile storedFile = storageService.storeForEnterprise(file, type, enterpriseId);
        return ResponseEntity.ok(new FileDTO(storedFile));
    }

    @GetMapping("/enterprise/{enterpriseId}")
    @ResponseBody
    public ResponseEntity<List<FileDTO>> getFilesByEnterprise(@PathVariable Long enterpriseId) {
        List<FileDTO> files = storageService.listByEnterprise(enterpriseId)
                .stream()
                .map(FileDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        storageService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<FileDTO> updateFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        
        StoredFile updatedFile = storageService.update(id, file);
        return ResponseEntity.ok(new FileDTO(updatedFile));
    }
    
    // Web endpoints for HTMX fragments
    @GetMapping("/property/{propertyId}/fragment")
    public String getDocumentsFragment(@PathVariable Long propertyId, 
                                     @RequestParam(value = "form", defaultValue = "false") boolean isForm,
                                     @RequestParam(value = "quick", defaultValue = "false") boolean quick,
                                     Model model) {
        if (quick) {
            // Quick load with minimal data for faster response
            List<FileListDTO> documents = storageService.listBasicInfoByPropertyAndType(propertyId, FileType.DOCUMENT);
            model.addAttribute("documents", documents);
            model.addAttribute("isQuickLoad", true);
        } else {
            // Full load with all data
            List<FileDTO> documents = storageService.listByPropertyAndType(propertyId, FileType.DOCUMENT)
                    .stream()
                    .map(FileDTO::new)
                    .collect(Collectors.toList());
            model.addAttribute("documents", documents);
            model.addAttribute("isQuickLoad", false);
        }
        model.addAttribute("propertyId", propertyId);
        return isForm ? "properties/fragments/documents-form" : "properties/fragments/documents";
    }
    
    @PostMapping("/upload/property/{propertyId}/fragment")
    public String uploadDocumentFragment(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long propertyId,
            @RequestParam(value = "fileType", defaultValue = "DOCUMENT") String fileTypeParam,
            @RequestParam(value = "form", defaultValue = "false") boolean isForm,
            Model model) {
        
        try {
            // Validate file type (allow common document types)
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.contains("pdf") && 
                !contentType.contains("doc") && 
                !contentType.contains("text") &&
                !contentType.contains("application/vnd.ms-excel") &&
                !contentType.contains("application/vnd.openxmlformats"))) {
                model.addAttribute("error", "Tipo de arquivo não permitido. Use PDF, DOC, DOCX, TXT, XLS ou XLSX.");
                return getDocumentsFragment(propertyId, isForm, false, model);
            }
            
            // Check file size (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                model.addAttribute("error", "Arquivo muito grande. Tamanho máximo: 10MB.");
                return getDocumentsFragment(propertyId, isForm, false, model);
            }
            
            FileType type = FileType.valueOf(fileTypeParam.toUpperCase());
            storageService.store(file, type, propertyId);
            model.addAttribute("success", "Documento enviado com sucesso!");
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao enviar documento: " + e.getMessage());
        }
        
        return getDocumentsFragment(propertyId, isForm, false, model);
    }
    
    @DeleteMapping("/{id}/fragment")
    public String deleteDocumentFragment(@PathVariable Long id, 
                                       @RequestParam(value = "form", defaultValue = "false") boolean isForm,
                                       Model model) {
        try {
            StoredFile file = storageService.getFileInfo(id);
            Long propertyId = file.getPropertyId();
            
            storageService.delete(id);
            model.addAttribute("success", "Documento removido com sucesso!");
            
            return getDocumentsFragment(propertyId, isForm, false, model);
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao remover documento: " + e.getMessage());
            return isForm ? "properties/fragments/documents-form" : "properties/fragments/documents";
        }
    }
    
    // Entity-based endpoints for both properties and projects
    @GetMapping("/entity/{entityId}/{entityType}/fragment")
    public String getEntityDocumentsFragment(@PathVariable Long entityId,
                                           @PathVariable String entityType,
                                           @RequestParam(value = "form", defaultValue = "false") boolean isForm,
                                           @RequestParam(value = "quick", defaultValue = "false") boolean quick,
                                           Model model) {
        try {
            EntityType entity = EntityType.valueOf(entityType.toUpperCase());
            
            if (quick) {
                // Quick load with minimal data for faster response
                List<FileListDTO> documents = storageService.listBasicInfoByEntityAndType(entityId, entity, FileType.DOCUMENT);
                model.addAttribute("documents", documents);
                model.addAttribute("isQuickLoad", true);
            } else {
                // Full load with all data
                List<FileDTO> documents = storageService.listByEntityAndType(entityId, entity, FileType.DOCUMENT)
                        .stream()
                        .map(FileDTO::new)
                        .collect(Collectors.toList());
                model.addAttribute("documents", documents);
                model.addAttribute("isQuickLoad", false);
            }
            
            model.addAttribute("entityId", entityId);
            model.addAttribute("entityType", entityType);
            
            // Return appropriate fragment based on entity type
            if (entity == EntityType.PROJECT) {
                return isForm ? "projects/fragments/documents-form" : "projects/fragments/documents";
            } else {
                return isForm ? "properties/fragments/documents-form" : "properties/fragments/documents";
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar documentos: " + e.getMessage());
            return isForm ? "properties/fragments/documents-form" : "properties/fragments/documents";
        }
    }
    
    @PostMapping("/upload/entity/{entityId}/fragment")
    public String uploadEntityDocumentFragment(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long entityId,
            @RequestParam(value = "fileType", defaultValue = "DOCUMENT") String fileTypeParam,
            @RequestParam(value = "entityType", defaultValue = "PROPERTY") String entityTypeParam,
            @RequestParam(value = "form", defaultValue = "false") boolean isForm,
            Model model) {
        
        try {
            // Validate file type (allow common document types)
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.contains("pdf") && 
                !contentType.contains("doc") && 
                !contentType.contains("text") &&
                !contentType.contains("application/vnd.ms-excel") &&
                !contentType.contains("application/vnd.openxmlformats"))) {
                model.addAttribute("error", "Tipo de arquivo não permitido. Use PDF, DOC, DOCX, TXT, XLS ou XLSX.");
                return getEntityDocumentsFragment(entityId, entityTypeParam, isForm, false, model);
            }
            
            // Check file size (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                model.addAttribute("error", "Arquivo muito grande. Tamanho máximo: 10MB.");
                return getEntityDocumentsFragment(entityId, entityTypeParam, isForm, false, model);
            }
            
            FileType fileType = FileType.valueOf(fileTypeParam.toUpperCase());
            EntityType entityType = EntityType.valueOf(entityTypeParam.toUpperCase());
            
            storageService.store(file, fileType, entityId, entityType);
            model.addAttribute("success", "Documento enviado com sucesso!");
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao enviar documento: " + e.getMessage());
        }
        
        return getEntityDocumentsFragment(entityId, entityTypeParam, isForm, false, model);
    }
    
    @DeleteMapping("/{id}/entity/fragment")
    public String deleteEntityDocumentFragment(@PathVariable Long id,
                                             @RequestParam(value = "form", defaultValue = "false") boolean isForm,
                                             Model model) {
        try {
            StoredFile file = storageService.getFileInfo(id);
            Long entityId = file.getEntityId();
            String entityType = file.getEntityType().toString();
            
            storageService.delete(id);
            model.addAttribute("success", "Documento removido com sucesso!");
            
            return getEntityDocumentsFragment(entityId, entityType, isForm, false, model);
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao remover documento: " + e.getMessage());
            return isForm ? "properties/fragments/documents-form" : "properties/fragments/documents";
        }
    }
}