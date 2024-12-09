package com.nailservices.repository;

import com.nailservices.entity.StudioReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface StudioReviewRepository extends JpaRepository<StudioReview, Long> {
    Page<StudioReview> findByStudioId(Long studioId, Pageable pageable);
    Optional<StudioReview> findByAppointmentId(Long appointmentId);
    boolean existsByAppointmentId(Long appointmentId);

    @Query("SELECT AVG(r.rating) FROM StudioReview r WHERE r.studio.id = :studioId")
    BigDecimal findAverageRatingByStudioId(Long studioId);

    @Query("SELECT COUNT(r) FROM StudioReview r WHERE r.studio.id = :studioId")
    Long countByStudioId(Long studioId);
}
