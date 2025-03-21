package com.nails.api.dto.service;

import com.nails.api.entity.NailService;
import com.nails.api.entity.ServiceImage;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NailServiceResponse {
    private Long id;
    private String name;
    private String description;
    private ServiceCategoryDTO category;
    private Integer duration;
    private BigDecimal price;
    private Boolean isActive;
    
    // Provider information
    private Long providerId;
    private String providerName;
    private String providerBusinessName;
    
    // Image information
    private List<String> imageUrls;
    private String primaryImageUrl;
    private Integer totalImages;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NailServiceResponse fromNailService(NailService service) {
        if (service == null) return null;

        NailServiceResponse response = new NailServiceResponse();
        response.setId(service.getId());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setDuration(service.getDuration());
        response.setPrice(service.getPrice());
        response.setIsActive(service.getIsActive());
        
        // Set provider information
        if (service.getProvider() != null) {
            response.setProviderId(service.getProvider().getId());
            response.setProviderName(service.getProvider().getFirstName() + " " + service.getProvider().getLastName());
            response.setProviderBusinessName(service.getProvider().getBusinessName());
        }
        
        // Set category information
        if (service.getCategory() != null) {
            response.setCategory(ServiceCategoryDTO.fromServiceCategory(service.getCategory()));
        }
        
        // Set image information
        List<ServiceImage> images = service.getImages();
        if (images != null && !images.isEmpty()) {
            response.setImageUrls(images.stream()
                    .map(ServiceImage::getImageUrl)
                    .collect(Collectors.toList()));
            response.setPrimaryImageUrl(images.get(0).getImageUrl());
            response.setTotalImages(images.size());
        }
        
        response.setCreatedAt(service.getCreatedAt());
        response.setUpdatedAt(service.getUpdatedAt());
        
        return response;
    }
}
