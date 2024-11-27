package com.nailservices.service;

import com.nailservices.dto.appointment.AppointmentRequest;
import com.nailservices.dto.appointment.AppointmentResponse;
import com.nailservices.dto.profile.ProfileResponse;
import com.nailservices.dto.service.NailServiceResponse;
import com.nailservices.entity.Appointment;
import com.nailservices.entity.AppointmentStatus;
import com.nailservices.entity.NailService;
import com.nailservices.entity.User;
import com.nailservices.exception.NailServicesException;
import com.nailservices.repository.AppointmentRepository;
import com.nailservices.repository.NailServiceRepository;
import com.nailservices.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final NailServiceRepository nailServiceRepository;

    @Transactional
    public AppointmentResponse createAppointment(Long customerId, AppointmentRequest request) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> NailServicesException.resourceNotFound("Customer not found"));

        User provider = userRepository.findById(request.getProviderId())
                .orElseThrow(() -> NailServicesException.resourceNotFound("Provider not found"));

        NailService service = nailServiceRepository.findById(request.getServiceId())
                .orElseThrow(() -> NailServicesException.resourceNotFound("Service not found"));

        // Validate service is active
        if (!service.getIsActive()) {
            throw NailServicesException.badRequest("This service is no longer available");
        }

        // Validate appointment time is in the future
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw NailServicesException.badRequest("Appointment time must be in the future");
        }

        // Validate service duration
        if (service.getDuration() == null || service.getDuration() < 15) {
            throw NailServicesException.badRequest("Invalid service duration. Duration must be at least 15 minutes");
        }
        if (service.getDuration() > 240) {
            throw NailServicesException.badRequest("Invalid service duration. Duration cannot exceed 4 hours");
        }

        // Calculate end time based on service duration
        LocalDateTime endTime = request.getStartTime().plusMinutes(service.getDuration());

        // Check for overlapping appointments
        if (appointmentRepository.hasOverlappingAppointments(provider.getId(), request.getStartTime(), endTime)) {
            throw NailServicesException.badRequest("Provider is not available at this time");
        }

        Appointment appointment = Appointment.builder()
                .customer(customer)
                .provider(provider)
                .service(service)
                .startTime(request.getStartTime())
                .endTime(endTime)
                .status(AppointmentStatus.PENDING)
                .notes(request.getNotes())
                .build();

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointment(Long appointmentId) {
        return mapToResponse(findAppointmentById(appointmentId));
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getCustomerAppointments(Long customerId, Pageable pageable) {
        return appointmentRepository.findByCustomerId(customerId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getProviderAppointments(Long providerId, Pageable pageable) {
        return appointmentRepository.findByProviderId(providerId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getProviderSchedule(Long providerId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findProviderAppointments(providerId, start, end)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = findAppointmentById(appointmentId);
        appointment.setStatus(status);
        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, Long userId) {
        Appointment appointment = findAppointmentById(appointmentId);
        
        // Verify that the user is either the customer or provider
        if (!appointment.getCustomer().getId().equals(userId) && 
            !appointment.getProvider().getId().equals(userId)) {
            throw NailServicesException.forbidden("You are not authorized to cancel this appointment");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING && 
            appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw NailServicesException.badRequest("Cannot cancel appointment with status: " + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    private Appointment findAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> NailServicesException.resourceNotFound("Appointment not found"));
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setCustomer(ProfileResponse.fromProfile(appointment.getCustomer().getProfile()));
        response.setProvider(ProfileResponse.fromProfile(appointment.getProvider().getProfile()));
        response.setService(NailServiceResponse.fromNailService(appointment.getService()));
        response.setStartTime(appointment.getStartTime());
        response.setEndTime(appointment.getEndTime());
        response.setStatus(appointment.getStatus());
        response.setNotes(appointment.getNotes());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setUpdatedAt(appointment.getUpdatedAt());
        return response;
    }
}
