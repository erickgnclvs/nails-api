package com.nailservices.controller.service;

import com.nailservices.dto.service.ServiceCategoryDTO;
import com.nailservices.service.service.ServiceCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class ServiceCategoryController {
    private final ServiceCategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROVIDER')")
    public ResponseEntity<List<ServiceCategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
