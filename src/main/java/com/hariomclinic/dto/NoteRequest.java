package com.hariomclinic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * NoteRequest DTO - Used when adding or editing a patient note/report.
 */
@Data
public class NoteRequest {

    @NotBlank(message = "Note content cannot be empty")
    private String content;
}
