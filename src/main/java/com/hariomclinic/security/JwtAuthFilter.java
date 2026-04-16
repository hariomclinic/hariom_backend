package com.hariomclinic.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JwtAuthFilter - Intercepts every HTTP request to validate JWT token.
 *
 * Extends OncePerRequestFilter -> guarantees this filter runs exactly once per request.
 *
 * Filter execution flow:
 *   1. Extract "Authorization" header from request
 *   2. Check if it starts with "Bearer "
 *   3. Extract the JWT token (after "Bearer ")
 *   4. Validate the token using JwtUtil
 *   5. Load UserDetails from DB using username from token
 *   6. Set Authentication in SecurityContext -> marks request as authenticated
 *   7. Call filterChain.doFilter() -> passes request to next filter/controller
 *
 * If token is missing or invalid -> SecurityContext remains empty -> Spring Security
 * returns 401 Unauthorized for protected endpoints.
 *
 * @RequiredArgsConstructor -> Lombok: generates constructor for all final fields
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Step 1: Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: Check Bearer prefix
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract token (skip "Bearer " prefix = 7 chars)
        String token = authHeader.substring(7);

        // Step 4: Validate token and check no existing auth in context
        if (jwtUtil.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 5: Extract username and load user from DB
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Step 6: Create authentication token and set in SecurityContext
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Step 7: Continue filter chain
        filterChain.doFilter(request, response);
    }
}
