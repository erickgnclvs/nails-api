package com.nails.api.service;

import com.nails.api.dto.service.NailServiceRequest;
import com.nails.api.dto.service.NailServiceResponse;
import com.nails.api.dto.service.ServiceCategoryDTO;
import com.nails.api.entity.NailService;
import com.nails.api.entity.Profile;
import com.nails.api.entity.ServiceCategory;
import com.nails.api.exception.NailsException;
import com.nails.api.repository.NailServiceRepository;
import com.nails.api.repository.ProfileRepository;
import com.nails.api.repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NailServiceService {
    private final NailServiceRepository nailServiceRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public NailServiceResponse createService(Long providerId, NailServiceRequest request) {
        Profile provider = profileRepository.findById(providerId)
            .orElseThrow(() -> NailsException.resourceNotFound("Provider"));

        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> NailsException.resourceNotFound("Category"));

        NailService service = new NailService();
        service.setProvider(provider);
        service.setCategory(category);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setDuration(request.getDuration());
        service.setPrice(request.getPrice());
        service.setIsActive(true);

        return convertToResponse(nailServiceRepository.save(service));
    }

    @Transactional(readOnly = true)
    public Page<NailServiceResponse> getProviderServices(Long providerId, Pageable pageable) {
        return nailServiceRepository.findByProviderIdAndIsActiveTrue(providerId, pageable)
            .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<NailServiceResponse> getServicesByCategory(Long categoryId, Pageable pageable) {
        return nailServiceRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable)
            .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<NailServiceResponse> searchServices(String searchTerm, Pageable pageable) {
        return nailServiceRepository.searchActiveServices(searchTerm, pageable)
            .map(this::convertToResponse);
    }

    @Transactional
    public NailServiceResponse updateService(Long serviceId, Long providerId, NailServiceRequest request) {
        NailService service = nailServiceRepository.findById(serviceId)
            .orElseThrow(() -> NailsException.resourceNotFound("Service"));

        if (!service.getProvider().getId().equals(providerId)) {
            throw NailsException.unauthorized("Not authorized to update this service");
        }

        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> NailsException.resourceNotFound("Category"));

        service.setCategory(category);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setDuration(request.getDuration());
        service.setPrice(request.getPrice());

        return convertToResponse(nailServiceRepository.save(service));
    }

    @Transactional
    public void deleteService(Long serviceId, Long providerId) {
        NailService service = nailServiceRepository.findById(serviceId)
            .orElseThrow(() -> NailsException.resourceNotFound("Service"));

        if (!service.getProvider().getId().equals(providerId)) {
            throw NailsException.unauthorized("Not authorized to delete this service");
        }

        service.setIsActive(false);
        nailServiceRepository.save(service);
    }

    @Transactional(readOnly = true)
    public NailServiceResponse getService(Long serviceId) {
        NailService service = nailServiceRepository.findById(serviceId)
            .orElseThrow(() -> NailsException.resourceNotFound("Service"));
        
        return convertToResponse(service);
    }

    private NailServiceResponse convertToResponse(NailService service) {
        NailServiceResponse response = new NailServiceResponse();
        response.setId(service.getId());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setDuration(service.getDuration());
        response.setPrice(service.getPrice());
        response.setIsActive(service.getIsActive());
        response.setCreatedAt(service.getCreatedAt());
        response.setUpdatedAt(service.getUpdatedAt());

        // Set provider information
        if (service.getProvider() != null) {
            response.setProviderId(service.getProvider().getId());
            response.setProviderName(service.getProvider().getFirstName() + " " + service.getProvider().getLastName());
            response.setProviderBusinessName(service.getProvider().getBusinessName());
        }

        // Set category information
        if (service.getCategory() != null) {
            ServiceCategoryDTO categoryDTO = new ServiceCategoryDTO();
            categoryDTO.setId(service.getCategory().getId());
            categoryDTO.setName(service.getCategory().getName());
            categoryDTO.setDescription(service.getCategory().getDescription());
            categoryDTO.setCreatedAt(service.getCategory().getCreatedAt());
            categoryDTO.setUpdatedAt(service.getCategory().getUpdatedAt());
            // We'll need to implement this count in the repository
            categoryDTO.setActiveServicesCount(null);
            response.setCategory(categoryDTO);
        }

        // Set image information
        if (service.getImages() != null) {
            response.setImageUrls(service.getImages().stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList()));
            
            service.getImages().stream()
                .filter(image -> Boolean.TRUE.equals(image.getIsPrimary()))
                .findFirst()
                .ifPresent(image -> response.setPrimaryImageUrl(image.getImageUrl()));
                
            response.setTotalImages(service.getImages().size());
        }

        return response;
    }
}
