package com.hariomclinic.config;

import com.hariomclinic.model.Admin;
import com.hariomclinic.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer - Seeds the database with default admin on first startup.
 *
 * Implements CommandLineRunner -> Spring Boot calls run() after application context loads.
 * This ensures the doctor/admin account exists before any login attempt.
 *
 * Default credentials:
 *   username: doctor
 *   password: hariom@2024
 *
 * The password is BCrypt-hashed before storing - never stored as plain text.
 *
 * @Slf4j -> Lombok: generates a logger field "log" for this class
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Delete old admin if exists, create new one with updated credentials
        adminRepository.findByUsername("doctor").ifPresent(adminRepository::delete);

        if (!adminRepository.findByUsername("drkalpesh").isPresent()) {
            Admin admin = new Admin();
            admin.setUsername("drkalpesh");
            admin.setPassword(passwordEncoder.encode("hariom7667")); // BCrypt hash
            admin.setFullName("Dr. Kalpesh Pashte");
            adminRepository.save(admin);
            log.info("Admin created: username=drkalpesh, password=hariom7667");
        }
    }
}
