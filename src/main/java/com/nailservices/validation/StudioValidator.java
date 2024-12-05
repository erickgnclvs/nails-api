package com.nailservices.validation;

import com.nailservices.dto.studio.StudioRequest;
import com.nailservices.entity.Studio;
import com.nailservices.entity.enums.StudioRole;
import com.nailservices.exception.NailServicesException;
import com.nailservices.repository.StudioProviderRepository;
import com.nailservices.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudioValidator {
    private final StudioRepository studioRepository;
    private final StudioProviderRepository studioProviderRepository;

    public void validateStudioCreation(StudioRequest request) {
        validateStudioName(request.getName(), null);
        validateContactInfo(request.getContactEmail(), request.getContactPhone());
    }

    public void validateStudioUpdate(Long studioId, StudioRequest request) {
        Studio existingStudio = studioRepository.findById(studioId)
            .orElseThrow(() -> NailServicesException.resourceNotFound("Studio"));
        
        if (!existingStudio.getName().equalsIgnoreCase(request.getName())) {
            validateStudioName(request.getName(), studioId);
        }
        validateContactInfo(request.getContactEmail(), request.getContactPhone());
    }

    public void validateProviderInvitation(Long studioId, Long providerId, StudioRole role) {
        // Check if provider is already in the studio
        if (studioProviderRepository.findByStudioIdAndProviderId(studioId, providerId).isPresent()) {
            throw NailServicesException.badRequest("Provider is already associated with this studio");
        }

        // Validate role assignment
        if (role == StudioRole.OWNER) {
            // Check if studio already has an owner
            boolean hasOwner = studioProviderRepository.findByStudioIdAndStatus(studioId, null).stream()
                .anyMatch(sp -> sp.getRole() == StudioRole.OWNER);
            if (hasOwner) {
                throw NailServicesException.badRequest("Studio already has an owner");
            }
        }
    }

    private void validateStudioName(String name, Long excludeStudioId) {
        if (name == null || name.trim().isEmpty()) {
            throw NailServicesException.validationError("Studio name cannot be empty");
        }

        // Check for unique name
        boolean nameExists = excludeStudioId == null ?
            studioRepository.existsByNameIgnoreCase(name) :
            studioRepository.findAll().stream()
                .anyMatch(s -> !s.getId().equals(excludeStudioId) && 
                             s.getName().equalsIgnoreCase(name));

        if (nameExists) {
            throw NailServicesException.validationError("Studio name already exists");
        }
    }

    private void validateContactInfo(String email, String phone) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw NailServicesException.validationError("Invalid email format");
        }

        if (phone == null || !phone.matches("^\\+?[1-9]\\d{1,14}$")) {
            throw NailServicesException.validationError("Invalid phone number format");
        }
    }
}
