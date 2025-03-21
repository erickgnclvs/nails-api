package com.nails.api.service;

import com.nails.api.dto.appointment.AppointmentRequest;
import com.nails.api.dto.appointment.AppointmentResponse;
import com.nails.api.dto.profile.ProfileResponse;
import com.nails.api.dto.service.NailServiceResponse;
import com.nails.api.entity.Appointment;
import com.nails.api.entity.enums.AppointmentStatus;
import com.nails.api.entity.NailService;
import com.nails.api.entity.TimeSlot;
import com.nails.api.entity.enums.TimeSlotStatus;
import com.nails.api.entity.User;
import com.nails.api.exception.NailsException;
import com.nails.api.repository.AppointmentRepository;
import com.nails.api.repository.NailServiceRepository;
import com.nails.api.repository.TimeSlotRepository;
import com.nails.api.repository.UserRepository;
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
    private final TimeSlotRepository timeSlotRepository;

    @Transactional
    public AppointmentResponse createAppointment(Long customerId, AppointmentRequest request) {
        // Get and validate customer
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> NailsException.resourceNotFound("Customer not found"));

        // Get and validate time slot
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> NailsException.resourceNotFound("Time slot not found"));

        // Validate time slot belongs to provider
        if (!timeSlot.getProvider().getId().equals(request.getProviderId())) {
            throw NailsException.badRequest("Time slot does not belong to the specified provider");
        }

        // Validate time slot is available
        if (timeSlot.getStatus() != TimeSlotStatus.AVAILABLE) {
            throw NailsException.badRequest("Selected time slot is not available");
        }

        // Validate time slot is in the future
        if (timeSlot.getStartTime().isBefore(LocalDateTime.now())) {
            throw NailsException.badRequest("Cannot book past time slots");
        }

        // Get and validate service
        NailService service = nailServiceRepository.findById(request.getServiceId())
                .orElseThrow(() -> NailsException.resourceNotFound("Service not found"));

        if (!service.getIsActive()) {
            throw NailsException.badRequest("This service is no longer available");
        }

        // Create appointment
        Appointment appointment = Appointment.builder()
                .customer(customer)
                .provider(timeSlot.getProvider())
                .service(service)
                .timeSlot(timeSlot)
                .status(AppointmentStatus.PENDING)
                .notes(request.getNotes())
                .build();

        // Update time slot status
        timeSlot.setStatus(TimeSlotStatus.BOOKED);
        timeSlotRepository.save(timeSlot);

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
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> NailsException.resourceNotFound("Appointment not found"));

        // If appointment is already in final state, don't allow updates
        if (appointment.getStatus() == AppointmentStatus.COMPLETED || 
            appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw NailsException.badRequest("Cannot update appointment in final state: " + appointment.getStatus());
        }

        // Update time slot status based on appointment status
        TimeSlot timeSlot = timeSlotRepository.findById(appointment.getTimeSlot().getId())
                .orElseThrow(() -> NailsException.resourceNotFound("Time slot not found"));

        switch (newStatus) {
            case CANCELLED:
                timeSlot.setStatus(TimeSlotStatus.AVAILABLE);
                break;
            case COMPLETED:
                timeSlot.setStatus(TimeSlotStatus.PAST);
                break;
            case CONFIRMED:
                timeSlot.setStatus(TimeSlotStatus.BOOKED);
                break;
            default:
                // For other statuses, keep the current time slot status
                break;
        }

        // Update appointment status
        appointment.setStatus(newStatus);
        
        // Save changes
        timeSlotRepository.save(timeSlot);
        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> NailsException.resourceNotFound("Appointment not found"));

        // Only allow cancellation of pending or confirmed appointments
        if (appointment.getStatus() != AppointmentStatus.PENDING && 
            appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw NailsException.badRequest("Cannot cancel appointment with status: " + appointment.getStatus());
        }

        // Update time slot status
        TimeSlot timeSlot = timeSlotRepository.findById(appointment.getTimeSlot().getId())
                .orElseThrow(() -> NailsException.resourceNotFound("Time slot not found"));
        
        timeSlot.setStatus(TimeSlotStatus.AVAILABLE);
        timeSlotRepository.save(timeSlot);

        // Update appointment
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setNotes(appointment.getNotes() + "\nCancellation reason: " + reason);
        appointmentRepository.save(appointment);
    }

    private Appointment findAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> NailsException.resourceNotFound("Appointment not found"));
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .customer(ProfileResponse.fromProfile(appointment.getCustomer().getProfile()))
                .provider(ProfileResponse.fromProfile(appointment.getProvider().getProfile()))
                .service(NailServiceResponse.fromNailService(appointment.getService()))
                .timeSlotId(appointment.getTimeSlot().getId())
                .startTime(appointment.getTimeSlot().getStartTime())
                .endTime(appointment.getTimeSlot().getEndTime())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
}
