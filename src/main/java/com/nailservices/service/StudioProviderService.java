package com.nailservices.service;

import com.nailservices.dto.studio.StudioProviderDTO;
import com.nailservices.entity.*;
import com.nailservices.entity.enums.StudioProviderStatus;
import com.nailservices.entity.enums.StudioRole;
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
public class StudioProviderService {
    private final StudioProviderRepository studioProviderRepository;
    private final StudioRepository studioRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public StudioProviderDTO inviteProvider(Long studioId, Long providerId, StudioRole role) {
        // Validate studio and provider exist
        Studio studio = studioRepository.findById(studioId)
            .orElseThrow(() -> new EntityNotFoundException("Studio not found"));
        Profile provider = profileRepository.findById(providerId)
            .orElseThrow(() -> new EntityNotFoundException("Provider not found"));

        // Check if provider is already in studio
        if (studioProviderRepository.findByStudioIdAndProviderId(studioId, providerId).isPresent()) {
            throw new IllegalStateException("Provider is already associated with this studio");
        }

        // Create studio provider association
        StudioProvider studioProvider = new StudioProvider();
        studioProvider.setId(new StudioProviderId(studioId, providerId));
        studioProvider.setStudio(studio);
        studioProvider.setProvider(provider);
        studioProvider.setRole(role);
        studioProvider.setStatus(StudioProviderStatus.PENDING);

        studioProvider = studioProviderRepository.save(studioProvider);
        return convertToDTO(studioProvider);
    }

    @Transactional
    public StudioProviderDTO updateProviderStatus(Long studioId, Long providerId, StudioProviderStatus status) {
        StudioProvider studioProvider = studioProviderRepository
            .findByStudioIdAndProviderId(studioId, providerId)
            .orElseThrow(() -> new EntityNotFoundException("Studio provider association not found"));

        studioProvider.setStatus(status);
        studioProvider = studioProviderRepository.save(studioProvider);
        return convertToDTO(studioProvider);
    }

    @Transactional
    public StudioProviderDTO updateProviderRole(Long studioId, Long providerId, StudioRole role) {
        StudioProvider studioProvider = studioProviderRepository
            .findByStudioIdAndProviderId(studioId, providerId)
            .orElseThrow(() -> new EntityNotFoundException("Studio provider association not found"));

        studioProvider.setRole(role);
        studioProvider = studioProviderRepository.save(studioProvider);
        return convertToDTO(studioProvider);
    }

    @Transactional
    public void removeProvider(Long studioId, Long providerId) {
        StudioProviderId id = new StudioProviderId(studioId, providerId);
        if (!studioProviderRepository.existsById(id)) {
            throw new EntityNotFoundException("Studio provider association not found");
        }
        studioProviderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<StudioProviderDTO> getStudioProviders(Long studioId) {
        return studioProviderRepository.findByStudioIdAndStatus(studioId, StudioProviderStatus.ACTIVE)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private StudioProviderDTO convertToDTO(StudioProvider studioProvider) {
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
