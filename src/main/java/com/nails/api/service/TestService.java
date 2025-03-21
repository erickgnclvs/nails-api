package com.nails.api.service;

import com.nails.api.repository.ProfileRepository;
import com.nails.api.repository.UserRepository;
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
            entityManager.createNativeQuery("DELETE FROM appointments").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM time_slots").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM weekly_schedules").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM service_images").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM nail_services").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM service_categories").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM profiles").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
            
            // Reset sequences
            entityManager.createNativeQuery("ALTER SEQUENCE appointments_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE time_slots_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE weekly_schedules_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE service_images_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE nail_services_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE service_categories_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE profiles_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE users_id_seq RESTART WITH 1").executeUpdate();
            
            // Re-insert default service categories
            entityManager.createNativeQuery(
                "INSERT INTO service_categories (name, description) VALUES " +
                "('Manicure', 'Basic to luxury nail care for hands'), " +
                "('Pedicure', 'Foot and toenail care services'), " +
                "('Nail Art', 'Decorative and artistic nail designs'), " +
                "('Nail Extensions', 'Acrylic, gel, and other nail extension services'), " +
                "('Nail Repair', 'Fixing broken or damaged nails')"
            ).executeUpdate();
            
            // Flush and clear the persistence context
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset database: " + e.getMessage(), e);
        }
    }
}
