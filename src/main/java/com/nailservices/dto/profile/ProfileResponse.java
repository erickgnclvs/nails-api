package com.nailservices.dto.profile;

import com.nailservices.entity.Profile;
import com.nailservices.entity.enums.ProfileType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
