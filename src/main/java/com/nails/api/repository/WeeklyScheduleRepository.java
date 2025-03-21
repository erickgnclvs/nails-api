package com.nails.api.repository;

import com.nails.api.entity.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, Long> {
    List<WeeklySchedule> findByProviderId(Long providerId);
    
    Optional<WeeklySchedule> findByProviderIdAndDayOfWeek(Long providerId, java.time.DayOfWeek dayOfWeek);
    
    @Query("SELECT ws FROM WeeklySchedule ws WHERE ws.provider.id = :providerId AND ws.isWorkingDay = true")
    List<WeeklySchedule> findWorkingDaysByProviderId(Long providerId);
}
