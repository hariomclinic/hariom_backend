package com.hariomclinic;

import com.hariomclinic.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * JwtUtilTest - Unit tests for JWT token generation and validation.
 *
 * ReflectionTestUtils.setField() -> Injects @Value fields in tests
 *   since Spring context is not loaded here (pure unit test).
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Inject private @Value fields using reflection
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "HariOmClinicSecretKey2024VeryLongSecretKeyForJWTSigning");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = jwtUtil.generateToken("doctor");
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void extractUsername_returnsCorrectUsername() {
        String token = jwtUtil.generateToken("doctor");
        String username = jwtUtil.extractUsername(token);
        assertThat(username).isEqualTo("doctor");
    }

    @Test
    void validateToken_withValidToken_returnsTrue() {
        String token = jwtUtil.generateToken("doctor");
        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_withInvalidToken_returnsFalse() {
        assertThat(jwtUtil.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void validateToken_withTamperedToken_returnsFalse() {
        String token = jwtUtil.generateToken("doctor");
        String tampered = token + "tampered";
        assertThat(jwtUtil.validateToken(tampered)).isFalse();
    }
}
