package com.hariomclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Patient Entity - Represents a patient registered by the doctor.
 *
 * Relationships:
 * @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
 *   -> One patient can have many notes/reports
 *   -> CascadeType.ALL: any operation (save/delete) on Patient cascades to its Notes
 *   -> orphanRemoval = true: if a Note is removed from the list, it's deleted from DB
 *
 * @CreationTimestamp -> Automatically sets the field when entity is first persisted
 */
@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String location;

    // Timestamp when patient record was created
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // One patient -> many notes (reports/descriptions)
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PatientNote> notes = new ArrayList<>();

    // Sets createdAt before first save using JPA lifecycle hook
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
