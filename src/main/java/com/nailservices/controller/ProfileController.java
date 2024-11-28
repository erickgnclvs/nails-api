package com.nailservices.controller;

import com.nailservices.dto.profile.ProfileRequest;
import com.nailservices.dto.profile.ProfileResponse;
import com.nailservices.security.annotation.AuthenticatedAccess;
import com.nailservices.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@AuthenticatedAccess
public class ProfileController {

    @Autowired
    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.createProfile(email, request));
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.getProfileByEmail(email));
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.updateProfile(email, request));
    }
}
