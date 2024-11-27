package com.nailservices.repository;

import com.nailservices.entity.Appointment;
import com.nailservices.entity.AppointmentStatus;
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
           "AND a.startTime BETWEEN :start AND :end")
    List<Appointment> findProviderAppointments(Long providerId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.provider.id = :providerId " +
           "AND a.status = :status AND a.startTime >= :start")
    Page<Appointment> findProviderAppointmentsByStatus(Long providerId, AppointmentStatus status, 
                                                     LocalDateTime start, Pageable pageable);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
           "WHERE a.provider.id = :providerId AND a.status NOT IN ('CANCELLED', 'COMPLETED', 'NO_SHOW') " +
           "AND ((a.startTime <= :endTime AND a.endTime >= :startTime) " +
           "OR (a.startTime >= :startTime AND a.startTime < :endTime))")
    boolean hasOverlappingAppointments(Long providerId, LocalDateTime startTime, LocalDateTime endTime);
}
