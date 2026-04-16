package com.hariomclinic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hariomclinic.dto.LoginRequest;
import com.hariomclinic.dto.LoginResponse;
import com.hariomclinic.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthControllerTest - Unit tests for the login API.
 *
 * @SpringBootTest -> Loads full Spring application context for integration-style tests
 * @AutoConfigureMockMvc -> Configures MockMvc to simulate HTTP requests without a real server
 *
 * MockMvc -> Allows testing controllers without starting a real HTTP server.
 *   perform() -> simulates an HTTP request
 *   andExpect() -> asserts on the response
 *
 * @MockBean -> Replaces the real AuthService bean with a Mockito mock.
 *   when(...).thenReturn(...) -> defines mock behavior
 *
 * Test naming convention: methodName_scenario_expectedResult
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Jackson JSON serializer/deserializer

    @MockBean
    private AuthService authService;

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        // Arrange - set up mock response
        LoginRequest request = new LoginRequest();
        request.setUsername("doctor");
        request.setPassword("hariom@2024");

        LoginResponse mockResponse = new LoginResponse("mock-jwt-token", "doctor", "Dr. Kalpesh Pashte");
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        // Act + Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.username").value("doctor"))
                .andExpect(jsonPath("$.fullName").value("Dr. Kalpesh Pashte"));
    }

    @Test
    void login_withBlankUsername_returns400() throws Exception {
        // Arrange - invalid request (blank username triggers @NotBlank)
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("hariom@2024");

        // Act + Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username").exists());
    }

    @Test
    void login_withInvalidCredentials_returns400() throws Exception {
        // Arrange - mock service throws exception for bad credentials
        LoginRequest request = new LoginRequest();
        request.setUsername("doctor");
        request.setPassword("wrongpassword");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid username or password"));

        // Act + Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }
}
