package com.hariomclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * PatientNote Entity - Represents a single report/description entry for a patient.
 *
 * This is the "note page" concept - each note has:
 *   - content: the doctor's written report/description
 *   - createdAt: auto-set timestamp when note is created
 *   - updatedAt: auto-updated timestamp whenever note is edited
 *
 * @ManyToOne -> Many notes belong to one patient
 * @JoinColumn -> Foreign key column in patient_notes table pointing to patients.id
 *
 * @PrePersist  -> JPA lifecycle: runs before INSERT
 * @PreUpdate   -> JPA lifecycle: runs before UPDATE
 */
@Entity
@Table(name = "patient_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The actual report/description written by the doctor
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Auto-set when note is first created
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Auto-updated every time note content is edited
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Foreign key linking this note to its patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
