package com.nailservices.controller;

import com.nailservices.dto.ProfileRequest;
import com.nailservices.dto.ProfileResponse;
import com.nailservices.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private final ProfileService profileService;

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
