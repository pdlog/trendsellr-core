package com.trendsellr.application.usecase.user;

import com.trendsellr.domain.exception.UserAlreadyExistsException;
import com.trendsellr.domain.model.Metadata;
import com.trendsellr.domain.model.user.Role;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

/**
 * Use Case for registering a new user.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * @param user The user to be created.
     * @return The created user.
     */
    public User dispatch(final User user) {
        log.info("Registering new user with email '{}'", user.getEmail());

        if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("A user with email " + user.getEmail() + " already exists.");
        }

        user.setId(UUID.randomUUID().toString());

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        user.setRoles(Set.of(Role.USER));

        final Metadata metadata = Metadata.builder()
                .version(1)
                .build();

        user.setMetadata(metadata);

        return this.userRepository.save(user);
    }
}