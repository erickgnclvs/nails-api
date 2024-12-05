package com.nailservices.repository;

import com.nailservices.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    
    @Query("SELECT s FROM Studio s LEFT JOIN FETCH s.providers WHERE s.id = :id")
    Optional<Studio> findByIdWithProviders(@Param("id") Long id);
    
    @Query("SELECT DISTINCT s FROM Studio s " +
           "LEFT JOIN FETCH s.providers sp " +
           "LEFT JOIN FETCH sp.provider p " +
           "WHERE p.id = :providerId")
    List<Studio> findAllByProviderId(@Param("providerId") Long providerId);
    
    boolean existsByNameIgnoreCase(String name);
}
