package com.nailservices.dto.review;

import com.nailservices.dto.profile.ProfileResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private Long appointmentId;
    private ProfileResponse customer;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
