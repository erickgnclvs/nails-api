package com.nailservices.service;

import com.nailservices.dto.profile.ProfileRequest;
import com.nailservices.dto.profile.ProfileResponse;
import com.nailservices.entity.Profile;
import com.nailservices.entity.User;
import com.nailservices.exception.NailServicesException;
import com.nailservices.repository.ProfileRepository;
import com.nailservices.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    
    private final ProfileRepository profileRepository;

    @Transactional
    public ProfileResponse createProfile(String email, ProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> NailServicesException.resourceNotFound("User"));

        if (profileRepository.findByUserId(user.getId()).isPresent()) {
            throw NailServicesException.badRequest("Profile already exists for this user");
        }

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());
        profile.setProfileImageUrl(request.getProfileImageUrl());
        profile.setBusinessName(request.getBusinessName());

        profile = profileRepository.save(profile);
        return ProfileResponse.fromProfile(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> NailServicesException.resourceNotFound("User"));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> NailServicesException.resourceNotFound("Profile"));
                
        return ProfileResponse.fromProfile(profile);
    }

    @Transactional
    public ProfileResponse updateProfile(String email, ProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> NailServicesException.resourceNotFound("User"));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Profile newProfile = new Profile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());
        profile.setProfileImageUrl(request.getProfileImageUrl());
        profile.setBusinessName(request.getBusinessName());
        profile.setDescription(request.getDescription());

        profile = profileRepository.save(profile);
        return ProfileResponse.fromProfile(profile);
    }
}
