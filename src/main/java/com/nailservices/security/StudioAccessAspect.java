package com.nailservices.security;

import com.nailservices.entity.Profile;
import com.nailservices.exception.NailServicesException;
import com.nailservices.repository.ProfileRepository;
import com.nailservices.security.annotation.StudioAccess;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class StudioAccessAspect {
    private final ProfileRepository profileRepository;

    @Before("@annotation(studioAccess)")
    public void checkStudioAccess(JoinPoint joinPoint, StudioAccess studioAccess) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        Profile profile = profileRepository.findByUserId(userDetails.getId())
            .orElseThrow(() -> NailServicesException.resourceNotFound("Profile"));

        if (!profile.isStudioProvider()) {
            throw NailServicesException.forbidden("Only studio providers can access this resource");
        }
    }
}
