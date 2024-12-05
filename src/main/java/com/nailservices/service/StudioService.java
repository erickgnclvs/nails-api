package com.nailservices.service;

import com.nailservices.dto.studio.StudioRequest;
import com.nailservices.dto.studio.StudioResponse;
import com.nailservices.dto.studio.StudioProviderDTO;
import com.nailservices.entity.*;
import com.nailservices.entity.enums.StudioRole;
import com.nailservices.entity.enums.StudioProviderStatus;
import com.nailservices.repository.StudioRepository;
import com.nailservices.repository.StudioProviderRepository;
import com.nailservices.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;
    private final StudioProviderRepository studioProviderRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public StudioResponse createStudio(Long ownerId, StudioRequest request) {
        // Validate owner exists
        Profile owner = profileRepository.findById(ownerId)
            .orElseThrow(() -> new EntityNotFoundException("Provider not found"));

        // Check if studio name is unique
        if (studioRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Studio name already exists");
        }

        // Create studio
        Studio studio = new Studio();
        studio.setName(request.getName());
        studio.setDescription(request.getDescription());
        studio.setLogoUrl(request.getLogoUrl());
        studio.setAddress(request.getAddress());
        studio.setContactPhone(request.getContactPhone());
        studio.setContactEmail(request.getContactEmail());
        studio.setBusinessHours(request.getBusinessHours());
        
        studio = studioRepository.save(studio);

        // Add owner as studio provider
        StudioProvider studioProvider = new StudioProvider();
        studioProvider.setId(new StudioProviderId(studio.getId(), owner.getId()));
        studioProvider.setStudio(studio);
        studioProvider.setProvider(owner);
        studioProvider.setRole(StudioRole.OWNER);
        studioProvider.setStatus(StudioProviderStatus.ACTIVE);
        
        studioProviderRepository.save(studioProvider);

        return convertToResponse(studio);
    }

    @Transactional(readOnly = true)
    public StudioResponse getStudio(Long studioId) {
        Studio studio = studioRepository.findByIdWithProviders(studioId)
            .orElseThrow(() -> new EntityNotFoundException("Studio not found"));
        return convertToResponse(studio);
    }

    @Transactional
    public StudioResponse updateStudio(Long studioId, StudioRequest request) {
        Studio studio = studioRepository.findById(studioId)
            .orElseThrow(() -> new EntityNotFoundException("Studio not found"));

        // Check if new name is unique (if name is being changed)
        if (!studio.getName().equalsIgnoreCase(request.getName()) &&
            studioRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Studio name already exists");
        }

        studio.setName(request.getName());
        studio.setDescription(request.getDescription());
        studio.setLogoUrl(request.getLogoUrl());
        studio.setAddress(request.getAddress());
        studio.setContactPhone(request.getContactPhone());
        studio.setContactEmail(request.getContactEmail());
        studio.setBusinessHours(request.getBusinessHours());

        studio = studioRepository.save(studio);
        return convertToResponse(studio);
    }

    @Transactional
    public void deleteStudio(Long studioId) {
        if (!studioRepository.existsById(studioId)) {
            throw new EntityNotFoundException("Studio not found");
        }
        studioRepository.deleteById(studioId);
    }

    @Transactional(readOnly = true)
    public List<StudioResponse> getProviderStudios(Long providerId) {
        return studioRepository.findAllByProviderId(providerId).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    private StudioResponse convertToResponse(Studio studio) {
        StudioResponse response = new StudioResponse();
        response.setId(studio.getId());
        response.setName(studio.getName());
        response.setDescription(studio.getDescription());
        response.setLogoUrl(studio.getLogoUrl());
        response.setAddress(studio.getAddress());
        response.setContactPhone(studio.getContactPhone());
        response.setContactEmail(studio.getContactEmail());
        response.setBusinessHours(studio.getBusinessHours());
        response.setCreatedAt(studio.getCreatedAt());
        response.setUpdatedAt(studio.getUpdatedAt());

        // Convert providers
        List<StudioProviderDTO> providerDTOs = studio.getProviders().stream()
            .map(this::convertToProviderDTO)
            .collect(Collectors.toList());
        response.setProviders(providerDTOs);

        return response;
    }

    private StudioProviderDTO convertToProviderDTO(StudioProvider studioProvider) {
        StudioProviderDTO dto = new StudioProviderDTO();
        Profile provider = studioProvider.getProvider();
        
        dto.setProviderId(provider.getId());
        dto.setProviderName(provider.getFirstName() + " " + provider.getLastName());
        dto.setProviderEmail(provider.getUser().getEmail());
        dto.setProfileImageUrl(provider.getProfileImageUrl());
        dto.setRole(studioProvider.getRole());
        dto.setStatus(studioProvider.getStatus());
        dto.setJoinedAt(studioProvider.getJoinedAt());
        
        return dto;
    }
}
