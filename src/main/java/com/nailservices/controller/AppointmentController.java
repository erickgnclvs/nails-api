package com.nailservices.controller;

import com.nailservices.dto.appointment.AppointmentRequest;
import com.nailservices.dto.appointment.AppointmentResponse;
import com.nailservices.entity.AppointmentStatus;
import com.nailservices.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestAttribute Long userId,
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.createAppointment(userId, request));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.getAppointment(appointmentId));
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<AppointmentResponse>> getCustomerAppointments(
            @RequestAttribute Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getCustomerAppointments(userId, pageable));
    }

    @GetMapping("/provider")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<Page<AppointmentResponse>> getProviderAppointments(
            @RequestAttribute Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getProviderAppointments(userId, pageable));
    }

    @GetMapping("/provider/schedule")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<List<AppointmentResponse>> getProviderSchedule(
            @RequestAttribute Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(appointmentService.getProviderSchedule(userId, start, end));
    }

    @PatchMapping("/{appointmentId}/status")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(appointmentId, status));
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROVIDER')")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestAttribute Long userId) {
        appointmentService.cancelAppointment(appointmentId, userId);
        return ResponseEntity.noContent().build();
    }
}
