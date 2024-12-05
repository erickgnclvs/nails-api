package com.nailservices.service;

import com.nailservices.entity.enums.TimeSlotStatus;
import com.nailservices.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeSlotCleanupScheduler {
    
    private final TimeSlotRepository timeSlotRepository;
    
    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void cleanupPastTimeSlots() {
        log.info("Starting time slot cleanup task");
        LocalDateTime now = LocalDateTime.now();
        
        try {
            int updatedSlots = timeSlotRepository.updatePastSlotsStatus(now, TimeSlotStatus.PAST);
            log.info("Updated {} past time slots to PAST status", updatedSlots);
        } catch (Exception e) {
            log.error("Error during time slot cleanup: {}", e.getMessage(), e);
        }
    }
}
