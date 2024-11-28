package com.nailservices.service;

import com.nailservices.dto.timeslot.TimeSlotResponse;
import com.nailservices.entity.TimeSlot;
import com.nailservices.entity.TimeSlotStatus;
import com.nailservices.entity.WeeklySchedule;
import com.nailservices.exception.NailServicesException;
import com.nailservices.repository.TimeSlotRepository;
import com.nailservices.repository.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Transactional
    public void generateSlotsForProvider(Long providerId, LocalDate startDate, LocalDate endDate) {
        List<WeeklySchedule> schedules = weeklyScheduleRepository.findWorkingDaysByProviderId(providerId);
        if (schedules.isEmpty()) {
            throw NailServicesException.badRequest("No working days found for provider");
        }

        List<TimeSlot> slots = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            WeeklySchedule schedule = schedules.stream()
                    .filter(s -> s.getDayOfWeek() == dayOfWeek)
                    .findFirst()
                    .orElse(null);

            if (schedule != null && schedule.isWorkingDay()) {
                slots.addAll(generateDaySlots(schedule, currentDate));
            }
            currentDate = currentDate.plusDays(1);
        }

        timeSlotRepository.saveAll(slots);
    }

    private List<TimeSlot> generateDaySlots(WeeklySchedule schedule, LocalDate date) {
        List<TimeSlot> slots = new ArrayList<>();
        LocalDateTime currentStart = LocalDateTime.of(date, schedule.getStartTime());
        LocalDateTime dayEnd = LocalDateTime.of(date, schedule.getEndTime());

        while (currentStart.plusMinutes(schedule.getSlotDurationMinutes()).isBefore(dayEnd) || 
               currentStart.plusMinutes(schedule.getSlotDurationMinutes()).equals(dayEnd)) {
            
            LocalDateTime slotEnd = currentStart.plusMinutes(schedule.getSlotDurationMinutes());
            
            TimeSlot slot = TimeSlot.builder()
                    .provider(schedule.getProvider())
                    .startTime(currentStart)
                    .endTime(slotEnd)
                    .status(TimeSlotStatus.AVAILABLE)
                    .isEnabled(true)
                    .weeklySchedule(schedule)
                    .build();
            
            slots.add(slot);
            currentStart = slotEnd;
        }

        return slots;
    }

    @Transactional(readOnly = true)
    public List<TimeSlotResponse> getAvailableSlots(Long providerId, LocalDateTime startDate, LocalDateTime endDate) {
        return timeSlotRepository.findAvailableSlots(providerId, startDate, endDate, TimeSlotStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateSlotStatus(Long slotId, TimeSlotStatus status) {
        TimeSlot slot = timeSlotRepository.findById(slotId)
                .orElseThrow(() -> NailServicesException.resourceNotFound("Time slot not found"));
        
        if (slot.getStartTime().isBefore(LocalDateTime.now())) {
            throw NailServicesException.badRequest("Cannot update past time slots");
        }
        
        slot.setStatus(status);
        timeSlotRepository.save(slot);
    }

    private TimeSlotResponse mapToResponse(TimeSlot slot) {
        return TimeSlotResponse.builder()
                .id(slot.getId())
                .providerId(slot.getProvider().getId())
                .providerEmail(slot.getProvider().getEmail())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .status(slot.getStatus())
                .isEnabled(slot.isEnabled())
                .build();
    }
}
