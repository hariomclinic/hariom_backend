package com.hariomclinic.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * PatientRequest DTO - Used when creating or updating a patient record.
 *
 * Validation annotations ensure data integrity before hitting the service layer:
 * @NotBlank -> String must not be null/empty
 * @NotNull  -> Field must not be null
 * @Min/@Max -> Numeric range validation
 * @Pattern  -> Regex validation for mobile number format
 */
@Data
public class PatientRequest {

    @NotBlank(message = "Patient name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Age seems invalid")
    private Integer age;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit Indian mobile number")
    private String mobileNumber;

    @NotBlank(message = "Location is required")
    private String location;
}
