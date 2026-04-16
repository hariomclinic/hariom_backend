package com.hariomclinic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * LoginRequest DTO - Data Transfer Object for login API.
 *
 * DTOs are used to transfer data between client and server.
 * They decouple the API layer from the entity/database layer.
 *
 * @NotBlank -> Validation: field must not be null or empty (triggers 400 if violated)
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
