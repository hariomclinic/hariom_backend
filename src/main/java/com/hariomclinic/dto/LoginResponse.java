package com.hariomclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * LoginResponse DTO - Returned to client after successful login.
 *
 * Contains:
 *   - token: JWT token the client stores (localStorage/sessionStorage) and sends
 *            in every subsequent request as "Authorization: Bearer <token>"
 *   - username: for display purposes
 *   - fullName: doctor's name to show in the dashboard
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String fullName;
}
