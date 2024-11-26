package com.nailservices.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
}
