package com.nailservices.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String bio;
    private String phoneNumber;
    private String address;
    private String profileImageUrl;
    
    // Provider-specific fields
    private String businessName;
    private String description;
}
