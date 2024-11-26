package com.nailservices.dto;

import com.nailservices.model.Profile;
import com.nailservices.model.ProfileType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileResponse {
    private Long id;
    private String email;
    private ProfileType profileType;
    private String firstName;
    private String lastName;
    private String bio;
    private String phoneNumber;
    private String address;
    private String profileImageUrl;
    private String businessName;
    private String description;
    private Boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProfileResponse fromProfile(Profile profile) {
        ProfileResponse response = new ProfileResponse();
        response.setId(profile.getId());
        response.setEmail(profile.getUser().getEmail());
        response.setProfileType(profile.getProfileType());
        response.setFirstName(profile.getFirstName());
        response.setLastName(profile.getLastName());
        response.setBio(profile.getBio());
        response.setPhoneNumber(profile.getPhoneNumber());
        response.setAddress(profile.getAddress());
        response.setProfileImageUrl(profile.getProfileImageUrl());
        response.setBusinessName(profile.getBusinessName());
        response.setDescription(profile.getDescription());
        response.setIsVerified(profile.getIsVerified());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());
        return response;
    }
}
