package com.nails.api.controller;

import com.nails.api.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
    
    private final TestService testService;

    @PostMapping("/reset-db")
    public ResponseEntity<String> resetDatabase() {
        testService.resetDatabase();
        return ResponseEntity.ok("Database reset successful");
    }
}
