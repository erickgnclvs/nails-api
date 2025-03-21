package com.nails.api.controller;

import com.nails.api.dto.review.RatingStats;
import com.nails.api.dto.review.ReviewRequest;
import com.nails.api.dto.review.ReviewResponse;
import com.nails.api.security.annotation.AuthenticatedAccess;
import com.nails.api.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@AuthenticatedAccess
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/provider")
    public ResponseEntity<ReviewResponse> createProviderReview(
            @RequestAttribute Long userId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createProviderReview(userId, request));
    }

    @PostMapping("/studio")
    public ResponseEntity<ReviewResponse> createStudioReview(
            @RequestAttribute Long userId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createStudioReview(userId, request));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<Page<ReviewResponse>> getProviderReviews(
            @PathVariable Long providerId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getProviderReviews(providerId, pageable));
    }

    @GetMapping("/studio/{studioId}")
    public ResponseEntity<Page<ReviewResponse>> getStudioReviews(
            @PathVariable Long studioId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getStudioReviews(studioId, pageable));
    }

    @GetMapping("/provider/{providerId}/stats")
    public ResponseEntity<RatingStats> getProviderRatingStats(@PathVariable Long providerId) {
        return ResponseEntity.ok(reviewService.getProviderRatingStats(providerId));
    }

    @GetMapping("/studio/{studioId}/stats")
    public ResponseEntity<RatingStats> getStudioRatingStats(@PathVariable Long studioId) {
        return ResponseEntity.ok(reviewService.getStudioRatingStats(studioId));
    }
}
