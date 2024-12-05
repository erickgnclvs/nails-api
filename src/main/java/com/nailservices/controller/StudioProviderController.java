package com.nailservices.controller;

import com.nailservices.dto.studio.StudioProviderDTO;
import com.nailservices.entity.enums.StudioProviderStatus;
import com.nailservices.entity.enums.StudioRole;
import com.nailservices.security.annotation.StudioAccess;
import com.nailservices.service.StudioProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studios/{studioId}/providers")
@RequiredArgsConstructor
public class StudioProviderController {
    private final StudioProviderService studioProviderService;

    @PostMapping("/{providerId}")
    @StudioAccess
    public ResponseEntity<StudioProviderDTO> inviteProvider(
            @PathVariable Long studioId,
            @PathVariable Long providerId,
            @RequestParam StudioRole role) {
        return ResponseEntity.ok(studioProviderService.inviteProvider(studioId, providerId, role));
    }

    @PutMapping("/{providerId}/status")
    @StudioAccess
    public ResponseEntity<StudioProviderDTO> updateProviderStatus(
            @PathVariable Long studioId,
            @PathVariable Long providerId,
            @RequestParam StudioProviderStatus status) {
        return ResponseEntity.ok(studioProviderService.updateProviderStatus(studioId, providerId, status));
    }

    @PutMapping("/{providerId}/role")
    @StudioAccess
    public ResponseEntity<StudioProviderDTO> updateProviderRole(
            @PathVariable Long studioId,
            @PathVariable Long providerId,
            @RequestParam StudioRole role) {
        return ResponseEntity.ok(studioProviderService.updateProviderRole(studioId, providerId, role));
    }

    @DeleteMapping("/{providerId}")
    @StudioAccess
    public ResponseEntity<Void> removeProvider(
            @PathVariable Long studioId,
            @PathVariable Long providerId) {
        studioProviderService.removeProvider(studioId, providerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @StudioAccess
    public ResponseEntity<List<StudioProviderDTO>> getStudioProviders(@PathVariable Long studioId) {
        return ResponseEntity.ok(studioProviderService.getStudioProviders(studioId));
    }
}
