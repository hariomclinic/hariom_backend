package com.hariomclinic.controller;

import com.hariomclinic.dto.LoginRequest;
import com.hariomclinic.dto.LoginResponse;
import com.hariomclinic.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - Handles authentication endpoints.
 *
 * @RestController -> Combines @Controller + @ResponseBody
 *   Every method return value is serialized to JSON automatically.
 *
 * @RequestMapping -> Base URL prefix for all methods in this controller
 *
 * @PostMapping -> Maps HTTP POST requests to the method
 *
 * @Valid -> Triggers validation of the request body using annotations in LoginRequest DTO
 *   If validation fails -> Spring returns 400 Bad Request automatically
 *
 * ResponseEntity -> Wrapper that lets you control HTTP status code + response body
 *
 * Swagger annotations:
 * @Tag -> Groups endpoints in Swagger UI
 * @Operation -> Describes what the endpoint does
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login API for doctor/admin")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Public endpoint - no JWT required
     * Returns JWT token on successful authentication
     */
    @PostMapping("/login")
    @Operation(summary = "Admin Login", description = "Authenticate doctor and receive JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
