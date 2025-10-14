package com.trendsellr.domain.service;

import com.trendsellr.domain.model.user.User;

public interface JwtService {

    /**
     * Generates a new JWT token for a user.
     */
    String generateToken(final User user);

    /**
     * Validates a token and extracts the user ID.
     */
    String getUserIdFromToken(final String token);
}
