package com.hariomclinic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * PublicController - Public endpoints accessible without authentication.
 *
 * These endpoints serve the public-facing clinic website:
 *   - Clinic info (name, doctor, specializations)
 *   - Contact details
 *
 * No JWT required - permitted in SecurityConfig via /api/public/**
 */
@RestController
@RequestMapping("/api/public")
@Tag(name = "Public Info", description = "Public clinic information - no auth required")
public class PublicController {

    @GetMapping("/clinic-info")
    @Operation(summary = "Get clinic information for the public website")
    public ResponseEntity<Map<String, Object>> getClinicInfo() {
        return ResponseEntity.ok(Map.of(
            "clinicName", "Hari Om Clinic",
            "doctorName", "Dr. Kalpesh Pashte",
            "specialization", "Homeopathic Physician",
            "experience", "10+ Years of Experience",
            "phone", "92269 93128",
            "email", "hariomclinic7746@gmail.com",
            "address", "At Post Shirishpada, Tal: Wada, Dist: Palghar 421312",
            "whatsapp", "919226993128",
            "quote", "Health is Wealth"
        ));
    }
}
