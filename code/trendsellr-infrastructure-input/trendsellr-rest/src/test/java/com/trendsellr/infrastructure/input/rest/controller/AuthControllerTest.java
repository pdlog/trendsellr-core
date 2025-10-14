package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.application.usecase.user.LoginUserUseCase;
import com.trendsellr.application.usecase.user.RegisterUserUseCase;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.infrastructure.input.rest.dto.AuthResponseDTO;
import com.trendsellr.infrastructure.input.rest.dto.LoginRequestDTO;
import com.trendsellr.infrastructure.input.rest.dto.UserRegisterRequestDTO;
import com.trendsellr.infrastructure.input.rest.dto.UserResponseDTO;
import com.trendsellr.infrastructure.input.rest.mapper.user.UserDTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private LoginUserUseCase loginUserUseCase;

    @Mock
    private UserDTOMapper userDTOMapper;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Given a registration request, when registerUser is called, then should return 201 Created with user data")
    void givenRegistrationRequest_whenRegisterUser_thenShouldReturnCreated() {
        // Given
        final UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO();
        final User userToRegister = User.builder().build();
        final User registeredUser = User.builder().id("123").build();
        final UserResponseDTO responseDTO = new UserResponseDTO().id("123");

        when(this.userDTOMapper.toDomain(requestDTO)).thenReturn(userToRegister);
        when(this.registerUserUseCase.dispatch(userToRegister)).thenReturn(registeredUser);
        when(this.userDTOMapper.toDto(registeredUser)).thenReturn(responseDTO);

        // When
        final ResponseEntity<UserResponseDTO> response = this.authController.registerUser(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(this.registerUserUseCase).dispatch(userToRegister);
    }

    @Test
    @DisplayName("Given a login request, when loginUser is called, then should return 200 OK with JWT")
    void givenLoginRequest_whenLoginUser_thenShouldReturnOkWithToken() {
        // Given
        final LoginRequestDTO requestDTO = new LoginRequestDTO().email("test@test.com")
                .password("password");
        final String expectedToken = "jwt-token";

        when(this.loginUserUseCase.dispatch(requestDTO.getEmail(), requestDTO.getPassword())).thenReturn(
                expectedToken);

        // When
        final ResponseEntity<AuthResponseDTO> response = this.authController.loginUser(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().getToken());
        verify(this.loginUserUseCase).dispatch(requestDTO.getEmail(), requestDTO.getPassword());
    }
}