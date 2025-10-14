package com.trendsellr.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trendsellr.domain.model.user.Role;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtService jwtService;

    private static final String TEST_SECRET_KEY = "a-very-long-and-secure-test-secret-that-is-at-least-256-bits-long";

    @BeforeEach
    void setUp() {
        final long expirationTime = 3600000;
        this.jwtService = new JwtServiceImpl(TEST_SECRET_KEY, expirationTime);
    }

    @Test
    @DisplayName("Given a user, when generateToken is called, then should create a valid JWT")
    void givenUser_whenGenerateToken_thenShouldCreateValidJwt() {
        // Given
        final User user = User.builder()
                .id("user-id-123")
                .email("test@example.com")
                .roles(Set.of(Role.USER, Role.ADMIN))
                .build();

        // When
        final String token = this.jwtService.generateToken(user);

        // Then
        assertNotNull(token);

        final Algorithm algorithm = Algorithm.HMAC256(TEST_SECRET_KEY);
        final JWTVerifier verifier = JWT.require(algorithm).build();
        final DecodedJWT decodedJWT = verifier.verify(token);

        assertEquals(user.getEmail(), decodedJWT.getSubject());
        assertTrue(decodedJWT.getClaim("roles").asList(String.class).contains("USER"));
        assertTrue(decodedJWT.getClaim("roles").asList(String.class).contains("ADMIN"));
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));
    }

    @Test
    @DisplayName("Given a valid token, when getUserEmailFromToken is called, then should return the user email")
    void givenValidToken_whenGetUserIdFromToken_thenShouldReturnEmail() {
        // Given
        final User user = User.builder().id("user-id-123").email("test@example.com").roles(Set.of(Role.USER)).build();
        final String token = this.jwtService.generateToken(user);

        // When
        final String userEmail = this.jwtService.getUserIdFromToken(token);

        // Then
        assertEquals(user.getEmail(), userEmail);
    }

    @Test
    @DisplayName("Given an expired token, when getUserIdFromToken is called, then should throw TokenExpiredException")
    void givenExpiredToken_whenGetUserIdFromToken_thenShouldThrowException() {
        // Given
        final Algorithm algorithm = Algorithm.HMAC256(TEST_SECRET_KEY);
        final String expiredToken = JWT.create()
                .withSubject("user@example.com")
                .withExpiresAt(new Date(System.currentTimeMillis() - 10000)) // Expiró hace 10 segundos
                .sign(algorithm);

        // When & Then
        assertThrows(TokenExpiredException.class, () -> this.jwtService.getUserIdFromToken(expiredToken));
    }

    @Test
    @DisplayName("Given a token with an invalid signature, when getUserIdFromToken is called, then should throw SignatureVerificationException")
    void givenInvalidSignatureToken_whenGetUserIdFromToken_thenShouldThrowException() {
        // Given
        final Algorithm wrongAlgorithm = Algorithm.HMAC256("another-secret-key");
        final String tokenWithWrongSignature = JWT.create()
                .withSubject("user@example.com")
                .sign(wrongAlgorithm);

        // When & Then
        assertThrows(SignatureVerificationException.class, () -> this.jwtService.getUserIdFromToken(tokenWithWrongSignature));
    }
}