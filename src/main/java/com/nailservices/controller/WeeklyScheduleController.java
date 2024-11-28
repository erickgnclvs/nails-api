package com.nailservices.controller;

import com.nailservices.dto.schedule.WeeklyScheduleRequest;
import com.nailservices.dto.schedule.WeeklyScheduleResponse;
import com.nailservices.security.annotation.AuthenticatedAccess;
import com.nailservices.security.annotation.ProviderAccess;
import com.nailservices.service.WeeklyScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@AuthenticatedAccess
public class WeeklyScheduleController {
    private final WeeklyScheduleService weeklyScheduleService;

    @PostMapping
    @ProviderAccess
    public ResponseEntity<List<WeeklyScheduleResponse>> setSchedule(
            @RequestAttribute Long userId,
            @Valid @RequestBody WeeklyScheduleRequest request) {
        return ResponseEntity.ok(weeklyScheduleService.setSchedule(userId, request));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<WeeklyScheduleResponse>> getProviderSchedule(
            @PathVariable Long providerId) {
        return ResponseEntity.ok(weeklyScheduleService.getProviderSchedule(providerId));
    }

    @GetMapping("/provider/{providerId}/working-days")
    public ResponseEntity<List<WeeklyScheduleResponse>> getProviderWorkingDays(
            @PathVariable Long providerId) {
        return ResponseEntity.ok(weeklyScheduleService.getProviderWorkingDays(providerId));
    }
}