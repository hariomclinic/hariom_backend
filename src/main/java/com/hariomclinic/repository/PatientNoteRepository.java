package com.hariomclinic.repository;

import com.hariomclinic.model.PatientNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * PatientNoteRepository - Data Access Layer for PatientNote entity.
 *
 * findByPatientId() -> Derived query:
 *   SQL: SELECT * FROM patient_notes WHERE patient_id = ? ORDER BY created_at DESC
 *   "patient_id" comes from the @JoinColumn in PatientNote entity
 */
@Repository
public interface PatientNoteRepository extends JpaRepository<PatientNote, Long> {
    List<PatientNote> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
