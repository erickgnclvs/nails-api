package com.nails.api.security;

import com.nails.api.entity.Profile;
import com.nails.api.exception.NailsException;
import com.nails.api.repository.ProfileRepository;
import com.nails.api.security.annotation.StudioAccess;
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
            .orElseThrow(() -> NailsException.resourceNotFound("Profile"));

        if (!profile.isStudioProvider()) {
            throw NailsException.forbidden("Only studio providers can access this resource");
        }
    }
}
