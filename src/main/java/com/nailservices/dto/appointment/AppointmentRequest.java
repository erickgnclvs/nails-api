package com.nailservices.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    private String notes;
}
