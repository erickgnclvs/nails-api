package com.nails.api.controller;

import com.nails.api.dto.service.NailServiceRequest;
import com.nails.api.dto.service.NailServiceResponse;
import com.nails.api.security.annotation.AuthenticatedAccess;
import com.nails.api.security.annotation.ProviderAccess;
import com.nails.api.service.NailServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@AuthenticatedAccess
public class NailServiceController {
    private final NailServiceService serviceManager;

    @PostMapping
    @ProviderAccess
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
    @ProviderAccess
    public ResponseEntity<NailServiceResponse> updateService(
            @PathVariable Long serviceId,
            @RequestAttribute Long userId,
            @Valid @RequestBody NailServiceRequest request) {
        return ResponseEntity.ok(serviceManager.updateService(serviceId, userId, request));
    }

    @DeleteMapping("/{serviceId}")
    @ProviderAccess
    public ResponseEntity<Void> deleteService(
            @PathVariable Long serviceId,
            @RequestAttribute Long userId) {
        serviceManager.deleteService(serviceId, userId);
        return ResponseEntity.noContent().build();
    }
}
