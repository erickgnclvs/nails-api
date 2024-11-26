package com.nailservices.repository;

import com.nailservices.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    Optional<ServiceCategory> findByName(String name);
    boolean existsByName(String name);
    
    @Query("SELECT COUNT(ns) FROM NailService ns WHERE ns.category.id = :categoryId AND ns.isActive = true")
    Long countActiveServicesByCategory(@Param("categoryId") Long categoryId);
}
