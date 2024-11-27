package com.nailservices.service;

import com.nailservices.repository.ProfileRepository;
import com.nailservices.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void resetDatabase() {
        try {
            // Delete in correct order to handle foreign key constraints
            entityManager.createNativeQuery("DELETE FROM nail_services").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM profiles").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
            
            // Reset sequences if they exist
            entityManager.createNativeQuery("ALTER SEQUENCE nail_services_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE profiles_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE users_id_seq RESTART WITH 1").executeUpdate();
            
            // Flush and clear the persistence context
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset database: " + e.getMessage(), e);
        }
    }
}
