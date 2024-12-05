package com.nailservices.entity;

import com.nailservices.entity.enums.StudioRole;
import com.nailservices.entity.enums.StudioProviderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "studio_providers")
public class StudioProvider {
    @EmbeddedId
    private StudioProviderId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studioId")
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("providerId")
    @JoinColumn(name = "provider_id")
    private Profile provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudioRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudioProviderStatus status;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();
}
