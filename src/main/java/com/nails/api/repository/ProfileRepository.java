package com.nails.api.repository;

import com.nails.api.entity.Profile;
import com.nails.api.entity.enums.ProfileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    Page<Profile> findByProfileType(ProfileType profileType, Pageable pageable);
}
