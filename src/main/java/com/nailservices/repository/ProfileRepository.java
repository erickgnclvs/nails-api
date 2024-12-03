package com.nailservices.repository;

import com.nailservices.entity.Profile;
import com.nailservices.entity.ProfileType;
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
