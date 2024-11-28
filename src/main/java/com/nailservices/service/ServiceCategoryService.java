package com.nailservices.service;

import com.nailservices.dto.service.ServiceCategoryDTO;
import com.nailservices.entity.ServiceCategory;
import com.nailservices.repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCategoryService {
    private final ServiceCategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ServiceCategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private ServiceCategoryDTO convertToDTO(ServiceCategory category) {
        ServiceCategoryDTO dto = new ServiceCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        
        // Count active services in this category
        Long activeCount = categoryRepository.countActiveServicesByCategory(category.getId());
        dto.setActiveServicesCount(activeCount != null ? activeCount.intValue() : 0);
        
        return dto;
    }
}
