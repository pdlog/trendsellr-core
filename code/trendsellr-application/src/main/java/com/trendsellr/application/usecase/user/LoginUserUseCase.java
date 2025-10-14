package com.trendsellr.application.usecase.user;

import com.trendsellr.application.service.JwtServiceImpl;
import com.trendsellr.domain.exception.InvalidCredentialsException;
import com.trendsellr.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Use Case for user login.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;

    /**
     * Authenticates a user and generates a JWT.
     *
     * @param email    The user's email.
     * @param password The user's raw password.
     * @return The generated JWT token.
     */
    public String dispatch(final String email, final String password) {
        log.info("Attempting to log in user with email '{}'", email);

        return this.userRepository.findByEmail(email)
                .filter(user -> this.passwordEncoder.matches(password, user.getPassword()))
                .map(this.jwtService::generateToken)
                .orElseThrow(InvalidCredentialsException::new);
    }
}