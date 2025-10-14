package com.trendsellr.application.usecase.user;

import com.trendsellr.application.service.JwtServiceImpl;
import com.trendsellr.domain.exception.InvalidCredentialsException;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    private static final String RAW_PASSWORD = "password123";

    private static final String HASHED_PASSWORD = "hashed-password-abc";

    private static final String WRONG_PASSWORD = "wrong-password-xyz";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtServiceImpl jwtService;

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    @Test
    @DisplayName("Given valid credentials, when dispatch is called, then should return a JWT")
    void givenValidCredentials_whenDispatch_thenShouldReturnJwt() {
        // Given
        final String email = "user@example.com";
        final String expectedToken = "jwt-token-123";
        final User foundUser = User.builder().email(email).password(HASHED_PASSWORD).build();

        when(this.userRepository.findByEmail(email)).thenReturn(Optional.of(foundUser));
        when(this.passwordEncoder.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(this.jwtService.generateToken(foundUser)).thenReturn(expectedToken);

        // When
        final String result = this.loginUserUseCase.dispatch(email, RAW_PASSWORD);

        // Then
        assertEquals(expectedToken, result);
        verify(this.userRepository).findByEmail(email);
        verify(this.passwordEncoder).matches(RAW_PASSWORD, HASHED_PASSWORD);
        verify(this.jwtService).generateToken(foundUser);
    }

    @Test
    @DisplayName("Given a non-existent email, when dispatch is called, then should throw InvalidCredentialsException")
    void givenNonExistentEmail_whenDispatch_thenShouldThrowException() {
        // Given
        final String email = "nonexistent@example.com";
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> this.loginUserUseCase.dispatch(email, RAW_PASSWORD));

        verify(this.passwordEncoder, never()).matches(any(), any());
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Given an incorrect password, when dispatch is called, then should throw InvalidCredentialsException")
    void givenIncorrectPassword_whenDispatch_thenShouldThrowException() {
        // Given
        final String email = "user@example.com";
        final User foundUser = User.builder().email(email).password(HASHED_PASSWORD).build();

        when(this.userRepository.findByEmail(email)).thenReturn(Optional.of(foundUser));
        when(this.passwordEncoder.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> this.loginUserUseCase.dispatch(email, WRONG_PASSWORD));

        verify(this.jwtService, never()).generateToken(any());
    }
}