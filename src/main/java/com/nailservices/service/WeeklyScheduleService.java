package com.nailservices.service;

import com.nailservices.dto.schedule.WeeklyScheduleRequest;
import com.nailservices.dto.schedule.WeeklyScheduleResponse;
import com.nailservices.entity.User;
import com.nailservices.entity.enums.UserRole;
import com.nailservices.entity.WeeklySchedule;
import com.nailservices.exception.NailServicesException;
import com.nailservices.repository.UserRepository;
import com.nailservices.repository.WeeklyScheduleRepository;

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
                .orElseThrow(() -> NailServicesException.resourceNotFound("Provider not found"));

        if (!provider.getRole().equals(UserRole.PROVIDER)) {
            throw NailServicesException.badRequest("Only providers can set schedules");
        }

        List<WeeklySchedule> schedules = request.getSchedules().stream()
                .map(scheduleItem -> {
                    // Validate times
                    if (scheduleItem.getStartTime().isAfter(scheduleItem.getEndTime())) {
                        throw NailServicesException.badRequest("Start time must be before end time");
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
            throw NailServicesException.resourceNotFound("Provider not found");
        }
        return weeklyScheduleRepository.findByProviderId(providerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WeeklyScheduleResponse> getProviderWorkingDays(Long providerId) {
        if (!userRepository.existsById(providerId)) {
            throw NailServicesException.resourceNotFound("Provider not found");
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
