package com.trendsellr.domain.exception;

import com.trendsellr.domain.exception.error.UserDefaultError;

/**
 * Exception thrown when a user with the given criteria already exists.
 */
public class UserAlreadyExistsException extends GenericException {


    /**
     * {@link UserAlreadyExistsException} with a custom message.
     *
     * @param message Custom exception's message
     */
    public UserAlreadyExistsException(final String message) {
        this(UserDefaultError.USER_ALREADY_EXISTS.getTitle(), message);
    }

    /**
     * {@link UserAlreadyExistsException} with a custom title and message.
     *
     * @param title   Custom exception's title
     * @param message Custom exception's message
     */
    public UserAlreadyExistsException(final String title, final String message) {
        super(UserDefaultError.USER_ALREADY_EXISTS.getType(), title, message);
    }
}