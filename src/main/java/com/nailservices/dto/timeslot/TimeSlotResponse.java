package com.nailservices.dto.timeslot;

import com.nailservices.entity.enums.TimeSlotStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotResponse {
    private Long id;
    private Long providerId;
    private String providerEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TimeSlotStatus status;
    private boolean isEnabled;
}
