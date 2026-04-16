package com.hariomclinic.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * JwtUtil - Utility class for JWT token operations.
 *
 * JWT (JSON Web Token) Structure:
 *   Header.Payload.Signature
 *   - Header: algorithm type (HS256)
 *   - Payload: claims (username, issued-at, expiration)
 *   - Signature: HMAC-SHA256(base64(header) + "." + base64(payload), secret)
 *
 * Flow:
 *   1. Doctor logs in -> generateToken(username) -> returns JWT
 *   2. Frontend stores JWT in localStorage
 *   3. Every API call sends: "Authorization: Bearer <JWT>"
 *   4. JwtAuthFilter intercepts -> validateToken() -> extracts username -> sets SecurityContext
 *
 * @Component -> Spring-managed singleton bean
 * @Value     -> Injects value from application.properties
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Creates a signing key from the secret string using HMAC-SHA algorithm
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a JWT token for the given username.
     * Claims stored in token:
     *   - subject: username
     *   - issuedAt: current time
     *   - expiration: current time + expiration ms
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT token.
     * Parses the token, verifies signature, returns subject claim.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a JWT token:
     *   - Checks signature integrity
     *   - Checks expiration
     *   Returns true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
