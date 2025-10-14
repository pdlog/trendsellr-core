package com.trendsellr.application.usecase.user;

import com.trendsellr.domain.exception.UserAlreadyExistsException;
import com.trendsellr.domain.model.user.Role;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    private static final String RAW_PASSWORD = "password123";

    private static final String HASHED_PASSWORD = "hashed-password-abc";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    @DisplayName("Given a new user, when dispatch is called, then should save and return the registered user")
    void givenNewUser_whenDispatch_thenShouldSaveAndReturnUser() {
        // Given
        final User inputUser = User.builder()
                .email("newuser@example.com")
                .password(RAW_PASSWORD)
                .build();

        when(this.userRepository.findByEmail(inputUser.getEmail())).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(inputUser.getPassword())).thenReturn(HASHED_PASSWORD);
        when(this.userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        final User result = this.registerUserUseCase.dispatch(inputUser);

        // Then
        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository).save(userCaptor.capture());
        final User capturedUser = userCaptor.getValue();

        assertNotNull(capturedUser.getId());
        assertEquals(HASHED_PASSWORD, capturedUser.getPassword());
        assertNotNull(capturedUser.getMetadata());
        assertEquals(1, capturedUser.getMetadata().getVersion());
        assertTrue(capturedUser.getRoles().contains(Role.USER));
        assertEquals(capturedUser, result);
    }

    @Test
    @DisplayName("Given an existing email, when dispatch is called, then should throw UserAlreadyExistsException")
    void givenExistingEmail_whenDispatch_thenShouldThrowException() {
        // Given
        final User inputUser = User.builder().email("existing@example.com").build();
        when(this.userRepository.findByEmail(inputUser.getEmail())).thenReturn(
                Optional.of(User.builder().build()));

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> this.registerUserUseCase.dispatch(inputUser));

        verify(this.passwordEncoder, never()).encode(any());
        verify(this.userRepository, never()).save(any());
    }
}