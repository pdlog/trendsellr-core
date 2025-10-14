package com.trendsellr.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendsellr.BaseTestIT;
import com.trendsellr.domain.exception.error.SecurityDefaultError;
import com.trendsellr.domain.exception.error.UserDefaultError;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.repository.UserRepository;
import com.trendsellr.infrastructure.input.rest.dto.LoginRequestDTO;
import com.trendsellr.infrastructure.input.rest.dto.UserRegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AuthControllerTestIT extends BaseTestIT {

    private static final String REGISTER_URL = "/api/public/v1/auth/register";

    private static final String LOGIN_URL = "/api/public/v1/auth/login";

    private final WebApplicationContext context;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /register -> Given a valid new user, should return 201 Created and save user")
    void givenValidNewUser_whenRegister_thenShouldCreateUser() throws Exception {
        // Given
        final UserRegisterRequestDTO request = new UserRegisterRequestDTO()
                .email("newuser@example.com")
                .username("newuser")
                .password("Password123!");

        // When & Then
        this.mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email", is("newuser@example.com")));

        // Verification
        final User savedUser = this.userRepository.findByEmail("newuser@example.com").orElseThrow();
        assertNotNull(savedUser);
        assertTrue(this.passwordEncoder.matches("Password123!", savedUser.getPassword()));
    }

    @Test
    @DisplayName("POST /register -> Given an existing email, should return 409 Conflict")
    void givenExistingEmail_whenRegister_thenShouldReturnConflict() throws Exception {
        // Given
        this.userRepository.save(User.builder().email("existing@example.com").build());
        final UserRegisterRequestDTO request = new UserRegisterRequestDTO()
                .email("existing@example.com")
                .username("anotheruser")
                .password("Password123!");

        // When & Then
        this.mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is(UserDefaultError.USER_ALREADY_EXISTS.getType())));
    }

    @Test
    @DisplayName("POST /login -> Given valid credentials, should return 200 OK with a JWT")
    void givenValidCredentials_whenLogin_thenShouldReturnJwt() throws Exception {
        // Given
        this.userRepository.save(User.builder()
                .email("test@user.com")
                .password(this.passwordEncoder.encode("password"))
                .build());
        final LoginRequestDTO request = new LoginRequestDTO()
                .email("test@user.com")
                .password("password");

        // When & Then
        this.mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    @DisplayName("POST /login -> Given invalid credentials, should return 401 Unauthorized")
    void givenInvalidCredentials_whenLogin_thenShouldReturnUnauthorized() throws Exception {
        // Given
        final LoginRequestDTO request = new LoginRequestDTO()
                .email("nonexistent@user.com")
                .password("wrongpassword");

        // When & Then
        this.mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.type", is(SecurityDefaultError.INVALID_CREDENTIALS.getType())));
    }
}