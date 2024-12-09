package com.nailservices.service;

import com.nailservices.dto.review.RatingStats;
import com.nailservices.dto.review.ReviewRequest;
import com.nailservices.dto.review.ReviewResponse;
import com.nailservices.dto.profile.ProfileResponse;
import com.nailservices.entity.*;
import com.nailservices.entity.enums.AppointmentStatus;
import com.nailservices.exception.NailServicesException;
import com.nailservices.repository.AppointmentRepository;
import com.nailservices.repository.ProviderReviewRepository;
import com.nailservices.repository.StudioReviewRepository;
import com.nailservices.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ProviderReviewRepository providerReviewRepository;
    private final StudioReviewRepository studioReviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse createProviderReview(Long customerId, ReviewRequest request) {
        // Get and validate customer
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> NailServicesException.resourceNotFound("Customer not found"));

        Appointment appointment = validateAndGetAppointment(request.getAppointmentId(), customer);

        if (providerReviewRepository.existsByAppointmentId(appointment.getId())) {
            throw NailServicesException.badRequest("This appointment has already been reviewed");
        }

        ProviderReview review = new ProviderReview();
        review.setAppointment(appointment);
        review.setCustomer(appointment.getCustomer().getProfile());
        review.setProvider(appointment.getProvider().getProfile());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        ProviderReview savedReview = providerReviewRepository.save(review);
        return mapToReviewResponse(savedReview);
    }

    @Transactional
    public ReviewResponse createStudioReview(Long customerId, ReviewRequest request) {
        // Get and validate customer
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> NailServicesException.resourceNotFound("Customer not found"));

        Appointment appointment = validateAndGetAppointment(request.getAppointmentId(), customer);

        if (appointment.getProvider().getProfile().getStudio() == null) {
            throw NailServicesException.badRequest("This appointment was not performed at a studio");
        }

        if (studioReviewRepository.existsByAppointmentId(appointment.getId())) {
            throw NailServicesException.badRequest("This appointment has already been reviewed");
        }

        StudioReview review = new StudioReview();
        review.setAppointment(appointment);
        review.setCustomer(appointment.getCustomer().getProfile());
        review.setStudio(appointment.getProvider().getProfile().getStudio());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        StudioReview savedReview = studioReviewRepository.save(review);
        return mapToReviewResponse(savedReview);
    }

    private Appointment validateAndGetAppointment(Long appointmentId, User customer) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> NailServicesException.resourceNotFound("Appointment not found"));

        if (!appointment.getCustomer().getId().equals(customer.getId())) {
            throw NailServicesException.forbidden("You can only review your own appointments");
        }

        if (!AppointmentStatus.COMPLETED.equals(appointment.getStatus())) {
            throw NailServicesException.badRequest("Only completed appointments can be reviewed");
        }

        return appointment;
    }

    public Page<ReviewResponse> getProviderReviews(Long providerId, Pageable pageable) {
        return providerReviewRepository.findByProviderId(providerId, pageable)
                .map(this::mapToReviewResponse);
    }

    public Page<ReviewResponse> getStudioReviews(Long studioId, Pageable pageable) {
        return studioReviewRepository.findByStudioId(studioId, pageable)
                .map(this::mapToReviewResponse);
    }

    public RatingStats getProviderRatingStats(Long providerId) {
        BigDecimal averageRating = providerReviewRepository.findAverageRatingByProviderId(providerId);
        Long totalReviews = providerReviewRepository.countByProviderId(providerId);
        return new RatingStats(averageRating, totalReviews);
    }

    public RatingStats getStudioRatingStats(Long studioId) {
        BigDecimal averageRating = studioReviewRepository.findAverageRatingByStudioId(studioId);
        Long totalReviews = studioReviewRepository.countByStudioId(studioId);
        return new RatingStats(averageRating, totalReviews);
    }

    private ReviewResponse mapToReviewResponse(ProviderReview review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setAppointmentId(review.getAppointment().getId());
        response.setCustomer(ProfileResponse.builder()
                .id(review.getCustomer().getId())
                .firstName(review.getCustomer().getFirstName())
                .lastName(review.getCustomer().getLastName())
                .profileType(review.getCustomer().getProfileType())
                .build());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }

    private ReviewResponse mapToReviewResponse(StudioReview review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setAppointmentId(review.getAppointment().getId());
        response.setCustomer(ProfileResponse.builder()
                .id(review.getCustomer().getId())
                .firstName(review.getCustomer().getFirstName())
                .lastName(review.getCustomer().getLastName())
                .profileType(review.getCustomer().getProfileType())
                .build());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}
