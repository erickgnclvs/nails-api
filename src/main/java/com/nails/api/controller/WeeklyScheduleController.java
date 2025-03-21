package com.nails.api.controller;

import com.nails.api.dto.schedule.WeeklyScheduleRequest;
import com.nails.api.dto.schedule.WeeklyScheduleResponse;
import com.nails.api.security.annotation.AuthenticatedAccess;
import com.nails.api.security.annotation.ProviderAccess;
import com.nails.api.service.WeeklyScheduleService;
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