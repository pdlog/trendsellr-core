package com.trendsellr.domain.repository;

import com.trendsellr.domain.model.user.User;

import java.util.Optional;

/**
 * Defines the contract that infrastructure must implement.
 */
public interface UserRepository {

    /**
     * Saves a user (either creates a new one or updates an existing one).
     *
     * @param user The user to save.
     * @return The saved user.
     */
    User save(final User user);

    /**
     * Finds a user by their email address.
     *
     * @param email The email to search for.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    Optional<User> findByEmail(final String email);

    /**
     * Deletes all products from the repository.
     */
    void deleteAll();

}