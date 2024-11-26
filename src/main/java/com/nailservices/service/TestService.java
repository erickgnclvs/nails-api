package com.nailservices.service;

import com.nailservices.repository.ProfileRepository;
import com.nailservices.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
            // Use native queries to bypass JPA's optimistic locking
            entityManager.createNativeQuery("DELETE FROM profiles").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
            
            // Flush and clear the persistence context
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset database: " + e.getMessage(), e);
        }
    }
}
