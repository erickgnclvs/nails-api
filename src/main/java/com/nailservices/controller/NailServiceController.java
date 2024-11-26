package com.nailservices.controller;

import com.nailservices.dto.NailServiceRequest;
import com.nailservices.dto.NailServiceResponse;
import com.nailservices.service.NailServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class NailServiceController {
    private final NailServiceManager serviceManager;

    @PostMapping
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<NailServiceResponse> createService(
            @RequestAttribute Long userId,
            @Valid @RequestBody NailServiceRequest request) {
        return ResponseEntity.ok(serviceManager.createService(userId, request));
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<NailServiceResponse> getService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(serviceManager.getService(serviceId));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<Page<NailServiceResponse>> getProviderServices(
            @PathVariable Long providerId,
            Pageable pageable) {
        return ResponseEntity.ok(serviceManager.getProviderServices(providerId, pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<NailServiceResponse>> getServicesByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        return ResponseEntity.ok(serviceManager.getServicesByCategory(categoryId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NailServiceResponse>> searchServices(
            @RequestParam String query,
            Pageable pageable) {
        return ResponseEntity.ok(serviceManager.searchServices(query, pageable));
    }

    @PutMapping("/{serviceId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<NailServiceResponse> updateService(
            @PathVariable Long serviceId,
            @RequestAttribute Long userId,
            @Valid @RequestBody NailServiceRequest request) {
        return ResponseEntity.ok(serviceManager.updateService(serviceId, userId, request));
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<Void> deleteService(
            @PathVariable Long serviceId,
            @RequestAttribute Long userId) {
        serviceManager.deleteService(serviceId, userId);
        return ResponseEntity.noContent().build();
    }
}
