package com.nails.api.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class StudioProviderId implements Serializable {
    private Long studioId;
    private Long providerId;

    public StudioProviderId(Long studioId, Long providerId) {
        this.studioId = studioId;
        this.providerId = providerId;
    }
}
