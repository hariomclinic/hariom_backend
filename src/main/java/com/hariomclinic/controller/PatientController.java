package com.hariomclinic.controller;

import com.hariomclinic.dto.NoteRequest;
import com.hariomclinic.dto.PatientRequest;
import com.hariomclinic.dto.PatientResponse;
import com.hariomclinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * PatientController - REST API for patient and note management.
 *
 * All endpoints here require JWT token (protected by SecurityConfig).
 *
 * REST conventions used:
 *   GET    /api/patients          -> list all
 *   POST   /api/patients          -> create new
 *   GET    /api/patients/{id}     -> get one
 *   PUT    /api/patients/{id}     -> update
 *   DELETE /api/patients/{id}     -> delete
 *
 *   POST   /api/patients/{id}/notes          -> add note
 *   PUT    /api/patients/{id}/notes/{noteId} -> edit note
 *   DELETE /api/patients/{id}/notes/{noteId} -> delete note
 *
 * @PathVariable  -> Extracts {id} from URL path
 * @RequestParam  -> Extracts ?name=xyz query parameter
 * @RequestBody   -> Deserializes JSON body to DTO
 *
 * @SecurityRequirement -> Tells Swagger this endpoint needs Bearer token
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "CRUD operations for patients and their notes")
@SecurityRequirement(name = "Bearer Authentication")
public class PatientController {

    private final PatientService patientService;

    // ==================== PATIENT ENDPOINTS ====================

    @GetMapping
    @Operation(summary = "Get all patients", description = "Returns all patients sorted by newest first")
    public ResponseEntity<List<PatientResponse>> getAllPatients(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(patientService.searchPatients(search));
        }
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PostMapping
    @Operation(summary = "Create new patient")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        // 201 Created is the correct HTTP status for resource creation
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient details")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient and all their notes")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // ==================== NOTE ENDPOINTS ====================

    @PostMapping("/{patientId}/notes")
    @Operation(summary = "Add a new note/report for a patient")
    public ResponseEntity<PatientResponse.NoteResponse> addNote(
            @PathVariable Long patientId,
            @Valid @RequestBody NoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.addNote(patientId, request));
    }

    @PutMapping("/{patientId}/notes/{noteId}")
    @Operation(summary = "Edit an existing note")
    public ResponseEntity<PatientResponse.NoteResponse> updateNote(
            @PathVariable Long patientId,
            @PathVariable Long noteId,
            @Valid @RequestBody NoteRequest request) {
        return ResponseEntity.ok(patientService.updateNote(patientId, noteId, request));
    }

    @DeleteMapping("/{patientId}/notes/{noteId}")
    @Operation(summary = "Delete a specific note")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long patientId,
            @PathVariable Long noteId) {
        patientService.deleteNote(patientId, noteId);
        return ResponseEntity.noContent().build();
    }
}
