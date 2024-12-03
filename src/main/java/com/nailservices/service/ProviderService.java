package com.nailservices.service;

import com.nailservices.dto.profile.ProfileResponse;
import com.nailservices.entity.ProfileType;
import com.nailservices.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderService {
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public Page<ProfileResponse> getProviders(Pageable pageable) {
        return profileRepository.findByProfileType(ProfileType.PROVIDER, pageable)
                .map(ProfileResponse::fromProfile);
    }
}
