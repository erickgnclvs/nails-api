package com.nails.api.repository;

import com.nails.api.entity.ProviderReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProviderReviewRepository extends JpaRepository<ProviderReview, Long> {
    Page<ProviderReview> findByProviderId(Long providerId, Pageable pageable);
    Optional<ProviderReview> findByAppointmentId(Long appointmentId);
    boolean existsByAppointmentId(Long appointmentId);

    @Query("SELECT AVG(r.rating) FROM ProviderReview r WHERE r.provider.id = :providerId")
    BigDecimal findAverageRatingByProviderId(Long providerId);

    @Query("SELECT COUNT(r) FROM ProviderReview r WHERE r.provider.id = :providerId")
    Long countByProviderId(Long providerId);
}
