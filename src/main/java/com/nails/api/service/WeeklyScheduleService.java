package com.nails.api.service;

import com.nails.api.dto.schedule.WeeklyScheduleRequest;
import com.nails.api.dto.schedule.WeeklyScheduleResponse;
import com.nails.api.entity.User;
import com.nails.api.entity.enums.UserRole;
import com.nails.api.entity.WeeklySchedule;
import com.nails.api.exception.NailsException;
import com.nails.api.repository.UserRepository;
import com.nails.api.repository.WeeklyScheduleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeeklyScheduleService {
    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<WeeklyScheduleResponse> setSchedule(Long providerId, WeeklyScheduleRequest request) {
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> NailsException.resourceNotFound("Provider not found"));

        if (!provider.getRole().equals(UserRole.PROVIDER)) {
            throw NailsException.badRequest("Only providers can set schedules");
        }

        List<WeeklySchedule> schedules = request.getSchedules().stream()
                .map(scheduleItem -> {
                    // Validate times
                    if (scheduleItem.getStartTime().isAfter(scheduleItem.getEndTime())) {
                        throw NailsException.badRequest("Start time must be before end time");
                    }

                    // Find existing schedule or create new one
                    WeeklySchedule schedule = weeklyScheduleRepository
                            .findByProviderIdAndDayOfWeek(providerId, scheduleItem.getDayOfWeek())
                            .orElse(new WeeklySchedule());

                    // Update schedule
                    schedule.setProvider(provider);
                    schedule.setDayOfWeek(scheduleItem.getDayOfWeek());
                    schedule.setStartTime(scheduleItem.getStartTime());
                    schedule.setEndTime(scheduleItem.getEndTime());
                    schedule.setWorkingDay(scheduleItem.isWorkingDay());
                    schedule.setSlotDurationMinutes(scheduleItem.getSlotDurationMinutes());

                    return weeklyScheduleRepository.save(schedule);
                })
                .collect(Collectors.toList());

        // Return a list of schedules' responses
        return schedules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WeeklyScheduleResponse> getProviderSchedule(Long providerId) {
        if (!userRepository.existsById(providerId)) {
            throw NailsException.resourceNotFound("Provider not found");
        }
        return weeklyScheduleRepository.findByProviderId(providerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WeeklyScheduleResponse> getProviderWorkingDays(Long providerId) {
        if (!userRepository.existsById(providerId)) {
            throw NailsException.resourceNotFound("Provider not found");
        }
        return weeklyScheduleRepository.findWorkingDaysByProviderId(providerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private WeeklyScheduleResponse mapToResponse(WeeklySchedule schedule) {
        return WeeklyScheduleResponse.builder()
                .id(schedule.getId())
                .providerId(schedule.getProvider().getId())
                .providerEmail(schedule.getProvider().getEmail())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .isWorkingDay(schedule.isWorkingDay())
                .slotDurationMinutes(schedule.getSlotDurationMinutes())
                .build();
    }
}
