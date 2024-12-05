package com.nailservices.repository;

import com.nailservices.entity.Appointment;
import com.nailservices.entity.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<Appointment> findByProviderId(Long providerId, Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.provider.id = :providerId " +
           "AND a.timeSlot.startTime BETWEEN :start AND :end")
    List<Appointment> findProviderAppointments(Long providerId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.provider.id = :providerId " +
           "AND a.status = :status AND a.timeSlot.startTime >= :start")
    Page<Appointment> findProviderAppointmentsByStatus(Long providerId, AppointmentStatus status, 
                                                     LocalDateTime start, Pageable pageable);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
           "WHERE a.provider.id = :providerId AND a.status NOT IN ('CANCELLED', 'COMPLETED', 'NO_SHOW') " +
           "AND ((a.timeSlot.startTime <= :endTime AND a.timeSlot.endTime >= :startTime) " +
           "OR (a.timeSlot.startTime >= :startTime AND a.timeSlot.startTime < :endTime))")
    boolean hasOverlappingAppointments(Long providerId, LocalDateTime startTime, LocalDateTime endTime);
}
