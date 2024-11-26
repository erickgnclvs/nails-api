package com.nailservices.service;

import com.nailservices.dto.ProfileResponse;
import com.nailservices.dto.ProfileRequest;
import com.nailservices.entity.User;
import com.nailservices.exception.NailServicesException;
import com.nailservices.model.Profile;
import com.nailservices.repository.ProfileRepository;
import com.nailservices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProfileRepository profileRepository;

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
