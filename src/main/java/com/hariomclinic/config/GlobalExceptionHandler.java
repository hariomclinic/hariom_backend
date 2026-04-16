package com.hariomclinic.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler - Centralized error handling for all controllers.
 *
 * @RestControllerAdvice -> Intercepts exceptions thrown from any @RestController.
 *   Instead of each controller handling errors, this single class handles them all.
 *
 * @ExceptionHandler(SomeException.class) -> Catches that specific exception type
 *   and returns a structured JSON error response.
 *
 * Why this matters:
 *   Without this, Spring returns ugly default error pages.
 *   With this, every error returns consistent JSON: { timestamp, status, message }
 *
 * Error response structure:
 *   {
 *     "timestamp": "2024-01-01T10:00:00",
 *     "status": 400,
 *     "message": "Validation failed",
 *     "errors": { "name": "Patient name is required" }
 *   }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles @Valid validation failures (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(field, message);
        });

        return ResponseEntity.badRequest().body(Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "status", 400,
            "message", "Validation failed",
            "errors", fieldErrors
        ));
    }

    // Handles business logic exceptions (404, auth errors, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = ex.getMessage().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "status", status.value(),
            "message", ex.getMessage()
        ));
    }

    // Catch-all for unexpected errors (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError().body(Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "status", 500,
            "message", "Internal server error"
        ));
    }
}
