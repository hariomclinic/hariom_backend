package com.hariomclinic.service;

import com.hariomclinic.dto.NoteRequest;
import com.hariomclinic.dto.PatientRequest;
import com.hariomclinic.dto.PatientResponse;
import com.hariomclinic.model.Patient;
import com.hariomclinic.model.PatientNote;
import com.hariomclinic.repository.PatientNoteRepository;
import com.hariomclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PatientService - Business logic for patient and note management.
 *
 * @Transactional -> Wraps method in a DB transaction.
 *   If any exception occurs mid-method, all DB changes are rolled back.
 *   Ensures data consistency (ACID properties).
 *
 * Mapper pattern:
 *   toResponse() -> converts Patient entity to PatientResponse DTO
 *   This keeps entity structure hidden from the API layer.
 *
 * Stream API usage:
 *   patients.stream().map(this::toResponse).collect(Collectors.toList())
 *   -> Functional-style transformation of entity list to DTO list
 */
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientNoteRepository noteRepository;

    // ==================== PATIENT CRUD ====================

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setMobileNumber(request.getMobileNumber());
        patient.setLocation(request.getLocation());
        Patient saved = patientRepository.save(patient);
        return toResponse(saved);
    }

    @Transactional(readOnly = true) // readOnly=true -> performance optimization for SELECT queries
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        Patient patient = findPatientOrThrow(id);
        return toResponse(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> searchPatients(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = findPatientOrThrow(id);
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setMobileNumber(request.getMobileNumber());
        patient.setLocation(request.getLocation());
        return toResponse(patientRepository.save(patient));
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = findPatientOrThrow(id);
        // CascadeType.ALL ensures all notes are deleted too
        patientRepository.delete(patient);
    }

    // ==================== NOTE CRUD ====================

    @Transactional
    public PatientResponse.NoteResponse addNote(Long patientId, NoteRequest request) {
        Patient patient = findPatientOrThrow(patientId);
        PatientNote note = new PatientNote();
        note.setContent(request.getContent());
        note.setPatient(patient);
        PatientNote saved = noteRepository.save(note);
        return toNoteResponse(saved);
    }

    @Transactional
    public PatientResponse.NoteResponse updateNote(Long patientId, Long noteId, NoteRequest request) {
        findPatientOrThrow(patientId); // Validate patient exists
        PatientNote note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + noteId));
        note.setContent(request.getContent());
        // @PreUpdate in entity auto-updates updatedAt timestamp
        return toNoteResponse(noteRepository.save(note));
    }

    @Transactional
    public void deleteNote(Long patientId, Long noteId) {
        findPatientOrThrow(patientId);
        noteRepository.deleteById(noteId);
    }

    // ==================== PRIVATE HELPERS ====================

    private Patient findPatientOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    // Entity -> DTO mapper
    private PatientResponse toResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setAge(patient.getAge());
        response.setMobileNumber(patient.getMobileNumber());
        response.setLocation(patient.getLocation());
        response.setCreatedAt(patient.getCreatedAt());
        response.setNotes(
            noteRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream()
                .map(this::toNoteResponse)
                .collect(Collectors.toList())
        );
        return response;
    }

    private PatientResponse.NoteResponse toNoteResponse(PatientNote note) {
        PatientResponse.NoteResponse nr = new PatientResponse.NoteResponse();
        nr.setId(note.getId());
        nr.setContent(note.getContent());
        nr.setCreatedAt(note.getCreatedAt());
        nr.setUpdatedAt(note.getUpdatedAt());
        return nr;
    }
}
