package com.nailservices.controller;

import com.nailservices.dto.profile.ProfileResponse;
import com.nailservices.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {
    private final ProviderService providerService;

    @GetMapping
    public ResponseEntity<Page<ProfileResponse>> getProviders(Pageable pageable) {
        return ResponseEntity.ok(providerService.getProviders(pageable));
    }
}
