package com.hariomclinic.repository;

import com.hariomclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * PatientRepository - Data Access Layer for Patient entity.
 *
 * Custom methods:
 *
 * findByNameContainingIgnoreCase() -> Derived query for search:
 *   SQL: SELECT * FROM patients WHERE LOWER(name) LIKE LOWER('%keyword%')
 *
 * findAllByOrderByCreatedAtDesc() -> Derived query for sorted list:
 *   SQL: SELECT * FROM patients ORDER BY created_at DESC
 *
 * @Query annotation -> Used for custom JPQL (Java Persistence Query Language) queries
 *   JPQL uses entity class names and field names, not table/column names
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Search patients by name (case-insensitive)
    List<Patient> findByNameContainingIgnoreCase(String name);

    // Get all patients sorted by newest first
    List<Patient> findAllByOrderByCreatedAtDesc();

    // Custom JPQL query example - count patients
    @Query("SELECT COUNT(p) FROM Patient p")
    Long countAllPatients();
}
