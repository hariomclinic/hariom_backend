package com.hariomclinic.config;

import com.hariomclinic.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

/**
 * SecurityConfig - Central Spring Security configuration.
 *
 * Key concepts:
 *
 * @Configuration -> Spring processes this class for bean definitions
 * @EnableWebSecurity -> Activates Spring Security's web security support
 *
 * SecurityFilterChain -> Defines which endpoints are public vs protected:
 *   - /api/auth/** -> PUBLIC (login endpoint, no token needed)
 *   - /api/public/** -> PUBLIC (clinic info for website visitors)
 *   - /swagger-ui/**, /api-docs/** -> PUBLIC (for API testing)
 *   - Everything else -> REQUIRES valid JWT token
 *
 * SessionCreationPolicy.STATELESS -> No HTTP sessions created.
 *   JWT is stateless: server doesn't store session, token carries all info.
 *
 * BCryptPasswordEncoder -> Hashes passwords with BCrypt algorithm (cost factor 10).
 *   BCrypt is one-way: you can verify but never decrypt.
 *
 * DaoAuthenticationProvider -> Connects UserDetailsService + PasswordEncoder
 *   for the authentication process.
 *
 * CORS (Cross-Origin Resource Sharing) -> Allows React frontend (localhost:3000)
 *   to call Spring backend (localhost:8080) - browsers block cross-origin by default.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF - not needed for stateless JWT APIs
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            // Add JWT filter BEFORE Spring's default username/password filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt with default strength 10 - industry standard for password hashing
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Explicitly allow the Netlify URL (and localhost for dev)
        config.setAllowedOrigins(List.of(frontendUrl, "http://localhost:3000", "http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
