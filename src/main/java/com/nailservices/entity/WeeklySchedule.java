package com.nailservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weekly_schedules",
    uniqueConstraints = @UniqueConstraint(columnNames = {"provider_id", "day_of_week"}))
public class WeeklySchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    @NotNull
    private User provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    @NotNull
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    @NotNull
    private LocalTime startTime;

    @Column(nullable = false)
    @NotNull
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean isWorkingDay;

    @Column(nullable = false)
    @Min(15)    // Minimum 15 minutes
    @Max(120)   // Maximum 2 hours
    private Integer slotDurationMinutes;

    @PrePersist
    @PreUpdate
    private void validateTimes() {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalStateException("Start time must be before end time");
        }
    }
}
