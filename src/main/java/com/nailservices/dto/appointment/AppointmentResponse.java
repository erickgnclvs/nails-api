package com.nailservices.dto.appointment;

import com.nailservices.dto.service.NailServiceResponse;
import com.nailservices.dto.profile.ProfileResponse;
import com.nailservices.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private ProfileResponse customer;
    private ProfileResponse provider;
    private NailServiceResponse service;
    private Long timeSlotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
