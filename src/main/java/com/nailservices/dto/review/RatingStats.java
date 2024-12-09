package com.nailservices.dto.review;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RatingStats {
    private BigDecimal averageRating;
    private Long totalReviews;

    public RatingStats(BigDecimal averageRating, Long totalReviews) {
        this.averageRating = averageRating != null ? averageRating : BigDecimal.ZERO;
        this.totalReviews = totalReviews != null ? totalReviews : 0L;
    }
}
