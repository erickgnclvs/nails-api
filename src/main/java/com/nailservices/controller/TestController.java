package com.nailservices.controller;

import com.nailservices.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/reset-db")
    public ResponseEntity<String> resetDatabase() {
        testService.resetDatabase();
        return ResponseEntity.ok("Database reset successful");
    }
}
