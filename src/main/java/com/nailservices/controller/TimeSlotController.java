package com.nailservices.controller;

import com.nailservices.dto.timeslot.TimeSlotResponse;
import com.nailservices.entity.TimeSlotStatus;
import com.nailservices.security.annotation.AuthenticatedAccess;
import com.nailservices.security.annotation.ProviderAccess;
import com.nailservices.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
@AuthenticatedAccess
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    @PostMapping("/generate")
    @ProviderAccess
    public ResponseEntity<Void> generateSlots(
            @RequestAttribute Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        timeSlotService.generateSlotsForProvider(userId, startDate, endDate);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/provider/{providerId}/available")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableSlots(
            @PathVariable Long providerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(timeSlotService.getAvailableSlots(providerId, startDate, endDate));
    }

    @PatchMapping("/{slotId}/status")
    @ProviderAccess
    public ResponseEntity<Void> updateSlotStatus(
            @PathVariable Long slotId,
            @RequestParam TimeSlotStatus status) {
        timeSlotService.updateSlotStatus(slotId, status);
        return ResponseEntity.ok().build();
    }
}
