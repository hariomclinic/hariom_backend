package com.hariomclinic.service;

import com.hariomclinic.dto.LoginRequest;
import com.hariomclinic.dto.LoginResponse;
import com.hariomclinic.model.Admin;
import com.hariomclinic.repository.AdminRepository;
import com.hariomclinic.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * AuthService - Business logic for authentication.
 *
 * Service Layer responsibility:
 *   - Contains business logic (not in Controller, not in Repository)
 *   - Orchestrates between Repository and Security components
 *   - Throws meaningful exceptions for error handling
 *
 * Login flow:
 *   1. AuthenticationManager.authenticate() -> calls CustomUserDetailsService.loadUserByUsername()
 *      -> fetches user from DB -> BCrypt compares passwords
 *   2. If authentication fails -> throws AuthenticationException -> 401 returned
 *   3. If success -> JwtUtil.generateToken() -> creates JWT
 *   4. Returns LoginResponse with token + user info
 *
 * @Service -> Marks as Spring service bean (business logic layer)
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        try {
            // This triggers Spring Security's full auth flow (load user + verify password)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }

        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        String token = jwtUtil.generateToken(admin.getUsername());

        return new LoginResponse(token, admin.getUsername(), admin.getFullName());
    }
}
