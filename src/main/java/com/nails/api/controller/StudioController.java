package com.nails.api.controller;

import com.nails.api.dto.studio.StudioRequest;
import com.nails.api.dto.studio.StudioResponse;
import com.nails.api.security.CustomUserDetails;
import com.nails.api.security.annotation.AuthenticatedAccess;
import com.nails.api.security.annotation.StudioAccess;
import com.nails.api.service.StudioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studios")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;

    @PostMapping
    @AuthenticatedAccess
    public ResponseEntity<StudioResponse> createStudio(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody StudioRequest request) {
        return ResponseEntity.ok(studioService.createStudio(userDetails.getId(), request));
    }

    @GetMapping("/{studioId}")
    @StudioAccess
    public ResponseEntity<StudioResponse> getStudio(@PathVariable Long studioId) {
        return ResponseEntity.ok(studioService.getStudio(studioId));
    }

    @PutMapping("/{studioId}")
    @StudioAccess
    public ResponseEntity<StudioResponse> updateStudio(
            @PathVariable Long studioId,
            @Valid @RequestBody StudioRequest request) {
        return ResponseEntity.ok(studioService.updateStudio(studioId, request));
    }

    @DeleteMapping("/{studioId}")
    @StudioAccess
    public ResponseEntity<Void> deleteStudio(@PathVariable Long studioId) {
        studioService.deleteStudio(studioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/provider/{providerId}")
    @AuthenticatedAccess
    public ResponseEntity<List<StudioResponse>> getProviderStudios(@PathVariable Long providerId) {
        return ResponseEntity.ok(studioService.getProviderStudios(providerId));
    }
}
