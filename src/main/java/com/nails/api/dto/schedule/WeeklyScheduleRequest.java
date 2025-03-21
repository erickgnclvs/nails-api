package com.nails.api.dto.schedule;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyScheduleRequest {
    @NotEmpty(message = "At least one schedule must be provided")
    private List<@Valid ScheduleItem> schedules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleItem {
        @NotNull(message = "Day of week is required")
        private DayOfWeek dayOfWeek;

        @NotNull(message = "Start time is required")
        private LocalTime startTime;

        @NotNull(message = "End time is required")
        private LocalTime endTime;

        private boolean isWorkingDay = true;

        @NotNull(message = "Slot duration is required")
        @Min(value = 15, message = "Slot duration must be at least 15 minutes")
        @Max(value = 120, message = "Slot duration cannot exceed 120 minutes")
        private Integer slotDurationMinutes;
    }
}
