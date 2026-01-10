package org.acabativa.rc.storage.controller;

import org.acabativa.rc.storage.dto.PhotoDTO;
import org.acabativa.rc.storage.entity.StoredFile;
import org.acabativa.rc.storage.entity.StoredFile.FileType;
import org.acabativa.rc.storage.service.StorageService;
import org.acabativa.rc.storage.util.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/photos")
public class PhotoController {
    
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png"
    );
    
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    
    private final StorageService storageService;
    private final ImageProcessor imageProcessor;
    
    @Autowired
    public PhotoController(StorageService storageService) {
        this.storageService = storageService;
        this.imageProcessor = new ImageProcessor();
    }
    
    @PostMapping("/upload/{propertyId}")
    @ResponseBody
    public ResponseEntity<PhotoDTO> uploadPhoto(
            @RequestParam("photo") MultipartFile file,
            @PathVariable Long propertyId) {
        
        // Validate image
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            return ResponseEntity.badRequest().build();
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().build();
        }
        
        StoredFile storedFile = storageService.store(file, FileType.PHOTO, propertyId);
        
        // Generate thumbnail asynchronously (in a real app)
        // For now, we'll just return the photo info
        
        return ResponseEntity.ok(new PhotoDTO(storedFile));
    }
    
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Resource> getPhoto(@PathVariable Long id) {
        try {
            StoredFile fileInfo = storageService.getFileInfo(id);
            
            if (fileInfo.getFileType() != FileType.PHOTO) {
                return ResponseEntity.notFound().build();
            }
            
            Resource file = storageService.loadAsResource(id);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                    .body(file);
        } catch (RuntimeException e) {
            // File not found or could not be read, return 404
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/thumbnail")
    @ResponseBody
    public ResponseEntity<Resource> getThumbnail(@PathVariable Long id) {
        // For now, return the same image
        // In production, you would generate and cache thumbnails
        try {
            return getPhoto(id);
        } catch (RuntimeException e) {
            // File not found or could not be read, return 404
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/info")
    @ResponseBody
    public ResponseEntity<PhotoDTO> getPhotoInfo(@PathVariable Long id) {
        StoredFile file = storageService.getFileInfo(id);
        
        if (file.getFileType() != FileType.PHOTO) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new PhotoDTO(file));
    }
    
    @GetMapping("/property/{propertyId}")
    @ResponseBody
    public ResponseEntity<List<PhotoDTO>> getPhotosByProperty(@PathVariable Long propertyId) {
        List<PhotoDTO> photos = storageService.listByPropertyAndType(propertyId, FileType.PHOTO)
                .stream()
                .map(PhotoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(photos);
    }
    
    @GetMapping("/property/{propertyId}/thumbnail")
    @ResponseBody
    public ResponseEntity<Resource> getPropertyThumbnail(@PathVariable Long propertyId) {
        List<StoredFile> photos = storageService.listByPropertyAndType(propertyId, FileType.PHOTO);
        
        if (photos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Try to get the first photo as thumbnail
        StoredFile firstPhoto = photos.get(0);
        try {
            Resource file = storageService.loadAsResource(firstPhoto.getId());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(firstPhoto.getContentType()))
                    .body(file);
        } catch (RuntimeException e) {
            // File not found or could not be read, return 404
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        StoredFile file = storageService.getFileInfo(id);
        
        if (file.getFileType() != FileType.PHOTO) {
            return ResponseEntity.notFound().build();
        }
        
        storageService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PhotoDTO> updatePhoto(
            @PathVariable Long id,
            @RequestParam("photo") MultipartFile file) {
        
        StoredFile existingFile = storageService.getFileInfo(id);
        
        if (existingFile.getFileType() != FileType.PHOTO) {
            return ResponseEntity.notFound().build();
        }
        
        // Validate new image
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            return ResponseEntity.badRequest().build();
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().build();
        }
        
        StoredFile updatedFile = storageService.update(id, file);
        return ResponseEntity.ok(new PhotoDTO(updatedFile));
    }
    
    // Web endpoints for HTMX fragments
    @GetMapping("/property/{propertyId}/fragment")
    public String getPhotosFragment(@PathVariable Long propertyId, 
                                  @RequestParam(value = "form", defaultValue = "false") boolean isForm,
                                  Model model) {
        List<PhotoDTO> photos = storageService.listByPropertyAndType(propertyId, FileType.PHOTO)
                .stream()
                .map(PhotoDTO::new)
                .collect(Collectors.toList());
        model.addAttribute("photos", photos);
        model.addAttribute("propertyId", propertyId);
        return isForm ? "properties/fragments/photos-form" : "properties/fragments/photos";
    }
    
    @PostMapping("/upload/{propertyId}/fragment")
    public String uploadPhotoFragment(
            @RequestParam("photo") MultipartFile file,
            @PathVariable Long propertyId,
            @RequestParam(value = "form", defaultValue = "false") boolean isForm,
            Model model) {
        
        try {
            // Validate image
            if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
                model.addAttribute("error", "Tipo de arquivo não permitido. Use apenas JPG ou PNG.");
                return getPhotosFragment(propertyId, isForm, model);
            }
            
            if (file.getSize() > MAX_FILE_SIZE) {
                model.addAttribute("error", "Arquivo muito grande. Tamanho máximo: 5MB.");
                return getPhotosFragment(propertyId, isForm, model);
            }
            
            storageService.store(file, FileType.PHOTO, propertyId);
            model.addAttribute("success", "Foto enviada com sucesso!");
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao enviar foto: " + e.getMessage());
        }
        
        return getPhotosFragment(propertyId, isForm, model);
    }
    
    @DeleteMapping("/{id}/fragment")
    public String deletePhotoFragment(@PathVariable Long id, 
                                    @RequestParam(value = "form", defaultValue = "false") boolean isForm,
                                    Model model) {
        try {
            StoredFile file = storageService.getFileInfo(id);
            
            if (file.getFileType() != FileType.PHOTO) {
                model.addAttribute("error", "Arquivo não encontrado.");
                return getPhotosFragment(file.getPropertyId(), isForm, model);
            }
            
            Long propertyId = file.getPropertyId();
            storageService.delete(id);
            model.addAttribute("success", "Foto removida com sucesso!");
            
            return getPhotosFragment(propertyId, isForm, model);
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao remover foto: " + e.getMessage());
            return isForm ? "properties/fragments/photos-form" : "properties/fragments/photos";
        }
    }
}