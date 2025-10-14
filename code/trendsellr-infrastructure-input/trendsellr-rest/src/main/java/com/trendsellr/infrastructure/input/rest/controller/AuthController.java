package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.application.usecase.user.LoginUserUseCase;
import com.trendsellr.application.usecase.user.RegisterUserUseCase;
import com.trendsellr.infrastructure.input.rest.api.AuthenticationApi;
import com.trendsellr.infrastructure.input.rest.dto.AuthResponseDTO;
import com.trendsellr.infrastructure.input.rest.dto.LoginRequestDTO;
import com.trendsellr.infrastructure.input.rest.dto.UserRegisterRequestDTO;
import com.trendsellr.infrastructure.input.rest.dto.UserResponseDTO;
import com.trendsellr.infrastructure.input.rest.mapper.user.UserDTOMapper;
import com.trendsellr.infrastructure.input.rest.security.AuthenticationRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthenticationApi {

    private final RegisterUserUseCase registerUserUseCase;

    private final LoginUserUseCase loginUserUseCase;

    private final UserDTOMapper userDTOMapper;

    @Override
    @PreAuthorize(AuthenticationRoles.ANY_USER)
    public ResponseEntity<UserResponseDTO> registerUser(final UserRegisterRequestDTO userRegisterRequestDTO) {
        log.info("REST request to register user: {}", userRegisterRequestDTO.getEmail());

        final var userToRegister = this.userDTOMapper.toDomain(userRegisterRequestDTO);

        final var registeredUser = this.registerUserUseCase.dispatch(userToRegister);

        final var responseDto = this.userDTOMapper.toDto(registeredUser);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize(AuthenticationRoles.ANY_USER)
    public ResponseEntity<AuthResponseDTO> loginUser(final LoginRequestDTO loginRequestDTO) {
        log.info("REST request to login user: {}", loginRequestDTO.getEmail());

        final String jwtToken = this.loginUserUseCase.dispatch(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        );

        final AuthResponseDTO authResponse = new AuthResponseDTO().token(jwtToken);

        return ResponseEntity.ok(authResponse);
    }
}