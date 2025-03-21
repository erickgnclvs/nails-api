package com.nails.api.repository;

import com.nails.api.entity.TimeSlot;
import com.nails.api.entity.enums.TimeSlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.provider.id = :providerId " +
           "AND ts.startTime >= :startDate AND ts.startTime < :endDate " +
           "AND ts.status = :status AND ts.isEnabled = true")
    List<TimeSlot> findAvailableSlots(Long providerId, LocalDateTime startDate, 
                                     LocalDateTime endDate, TimeSlotStatus status);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.provider.id = :providerId " +
           "AND ts.startTime >= :startDate AND ts.startTime < :endDate")
    List<TimeSlot> findSlotsByDateRange(Long providerId, LocalDateTime startDate, 
                                       LocalDateTime endDate);

    @Query("SELECT CASE WHEN COUNT(ts) > 0 THEN true ELSE false END FROM TimeSlot ts " +
           "WHERE ts.provider.id = :providerId AND ts.status = 'BOOKED' " +
           "AND ((ts.startTime <= :endTime AND ts.endTime > :startTime) " +
           "OR (ts.startTime < :endTime AND ts.endTime >= :endTime))")
    boolean hasOverlappingBookings(Long providerId, LocalDateTime startTime, LocalDateTime endTime);

    @Modifying
    @Query("UPDATE TimeSlot ts SET ts.status = :status WHERE ts.endTime < :now " +
           "AND ts.status NOT IN ('PAST', 'BLOCKED')")
    int updatePastSlotsStatus(LocalDateTime now, TimeSlotStatus status);

    Optional<TimeSlot> findByIdAndStatus(Long id, TimeSlotStatus status);
}
