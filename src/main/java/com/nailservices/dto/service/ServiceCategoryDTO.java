package com.nailservices.dto.service;

import com.nailservices.entity.ServiceCategory;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ServiceCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer activeServicesCount;

    public static ServiceCategoryDTO fromServiceCategory(ServiceCategory category) {
        if (category == null) return null;

        ServiceCategoryDTO dto = new ServiceCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        
        // Calculate active services count
        if (category.getServices() != null) {
            dto.setActiveServicesCount((int) category.getServices().stream()
                    .filter(service -> service.getIsActive() != null && service.getIsActive())
                    .count());
        } else {
            dto.setActiveServicesCount(0);
        }
        
        return dto;
    }
}
