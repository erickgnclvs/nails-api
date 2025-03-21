package com.nails.api.repository;

import com.nails.api.entity.NailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NailServiceRepository extends JpaRepository<NailService, Long> {
    Page<NailService> findByProviderId(Long providerId, Pageable pageable);
    
    Page<NailService> findByProviderIdAndIsActiveTrue(Long providerId, Pageable pageable);
    
    Page<NailService> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);
    
    @Query("SELECT ns FROM NailService ns WHERE ns.isActive = true AND " +
           "(LOWER(ns.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ns.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<NailService> searchActiveServices(String searchTerm, Pageable pageable);
    
    List<NailService> findByProviderIdAndIsActiveTrue(Long providerId);
}
