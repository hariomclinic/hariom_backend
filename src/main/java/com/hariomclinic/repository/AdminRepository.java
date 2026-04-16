package com.hariomclinic.repository;

import com.hariomclinic.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * AdminRepository - Data Access Layer for Admin entity.
 *
 * Extends JpaRepository<Admin, Long>:
 *   - Admin -> the entity type
 *   - Long  -> the type of the primary key
 *
 * JpaRepository provides out-of-the-box methods:
 *   - save(), findById(), findAll(), deleteById(), count(), etc.
 *
 * Custom query method:
 *   findByUsername() -> Spring Data JPA auto-generates SQL:
 *   "SELECT * FROM admin WHERE username = ?"
 *   This is called "Derived Query Method" - no SQL needed!
 *
 * @Repository -> Marks this as a Spring-managed bean, also enables exception translation
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}
