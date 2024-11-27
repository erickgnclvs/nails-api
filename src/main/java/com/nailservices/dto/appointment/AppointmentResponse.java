package com.nailservices.dto.appointment;

import com.nailservices.dto.profile.ProfileResponse;
import com.nailservices.dto.service.NailServiceResponse;
import com.nailservices.entity.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private ProfileResponse customer;
    private ProfileResponse provider;
    private NailServiceResponse service;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
