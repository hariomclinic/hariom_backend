package com.hariomclinic.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PatientResponse DTO - Returned to client when fetching patient data.
 *
 * Why a separate response DTO?
 * - Avoids exposing internal entity structure
 * - Controls exactly what data is sent to the frontend
 * - Prevents circular serialization issues (Patient -> Notes -> Patient...)
 */
@Data
public class PatientResponse {
    private Long id;
    private String name;
    private Integer age;
    private String mobileNumber;
    private String location;
    private LocalDateTime createdAt;
    private List<NoteResponse> notes;

    @Data
    public static class NoteResponse {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
