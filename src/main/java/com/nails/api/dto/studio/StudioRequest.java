package com.nails.api.dto.studio;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StudioRequest {
    @NotBlank(message = "Studio name is required")
    private String name;
    
    private String description;
    
    private String logoUrl;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String contactPhone;
    
    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;
    
    private String businessHours;  // JSON string of business hours
}
