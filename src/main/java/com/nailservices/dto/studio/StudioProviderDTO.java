package com.nailservices.dto.studio;

import com.nailservices.entity.enums.StudioRole;
import com.nailservices.entity.enums.StudioProviderStatus;
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
