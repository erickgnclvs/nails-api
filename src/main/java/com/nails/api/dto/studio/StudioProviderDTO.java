package com.nails.api.dto.studio;

import com.nails.api.entity.enums.StudioRole;
import com.nails.api.entity.enums.StudioProviderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudioProviderDTO {
    private Long providerId;
    private String providerName;
    private String providerEmail;
    private String profileImageUrl;
    private StudioRole role;
    private StudioProviderStatus status;
    private LocalDateTime joinedAt;
}
