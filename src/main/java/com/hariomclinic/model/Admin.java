package com.hariomclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin Entity - Represents the single doctor/admin user in the system.
 *
 * @Entity    -> Marks this class as a JPA entity (maps to a DB table)
 * @Table     -> Specifies the table name in MySQL
 * @Id        -> Marks the primary key field
 * @GeneratedValue -> Auto-increments the ID
 * @Column    -> Maps field to a specific column with constraints
 *
 * Lombok annotations:
 * @Data          -> Generates getters, setters, toString, equals, hashCode
 * @NoArgsConstructor -> Generates no-arg constructor (required by JPA)
 * @AllArgsConstructor -> Generates all-args constructor
 */
@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username used for login
    @Column(nullable = false, unique = true)
    private String username;

    // BCrypt hashed password stored in DB (never plain text)
    @Column(nullable = false)
    private String password;

    // Doctor's display name shown on the website
    @Column(nullable = false)
    private String fullName;
}
