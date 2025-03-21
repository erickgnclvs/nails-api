package com.nails.api.service;

import com.nails.api.dto.profile.ProfileRequest;
import com.nails.api.dto.profile.ProfileResponse;
import com.nails.api.entity.Profile;
import com.nails.api.entity.User;
import com.nails.api.exception.NailsException;
import com.nails.api.repository.ProfileRepository;
import com.nails.api.repository.UserRepository;
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
                .orElseThrow(() -> NailsException.resourceNotFound("User"));

        if (profileRepository.findByUserId(user.getId()).isPresent()) {
            throw NailsException.badRequest("Profile already exists for this user");
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
                .orElseThrow(() -> NailsException.resourceNotFound("User"));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> NailsException.resourceNotFound("Profile"));
                
        return ProfileResponse.fromProfile(profile);
    }

    @Transactional
    public ProfileResponse updateProfile(String email, ProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> NailsException.resourceNotFound("User"));

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
