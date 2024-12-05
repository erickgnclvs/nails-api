package com.nailservices.repository;

import com.nailservices.entity.StudioProvider;
import com.nailservices.entity.StudioProviderId;
import com.nailservices.entity.enums.StudioProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudioProviderRepository extends JpaRepository<StudioProvider, StudioProviderId> {
    
    @Query("SELECT sp FROM StudioProvider sp " +
           "WHERE sp.studio.id = :studioId AND sp.provider.id = :providerId")
    Optional<StudioProvider> findByStudioIdAndProviderId(
        @Param("studioId") Long studioId,
        @Param("providerId") Long providerId
    );
    
    List<StudioProvider> findByStudioIdAndStatus(Long studioId, StudioProviderStatus status);
    
    List<StudioProvider> findByProviderId(Long providerId);
    
    @Query("SELECT COUNT(sp) > 0 FROM StudioProvider sp " +
           "WHERE sp.studio.id = :studioId AND sp.provider.id = :providerId " +
           "AND sp.status = 'ACTIVE'")
    boolean isProviderActiveInStudio(
        @Param("studioId") Long studioId,
        @Param("providerId") Long providerId
    );
}
