package com.trendsellr.domain.exception;

import com.trendsellr.domain.exception.error.SecurityDefaultError;

/**
 * Exception thrown for invalid authentication credentials.
 */
public class InvalidCredentialsException extends GenericException {

    /**
     * {@link SecurityDefaultError} is used as default error.
     */
    public InvalidCredentialsException() {
        this(SecurityDefaultError.INVALID_CREDENTIALS.getMessage());
    }

    /**
     * {@link InvalidCredentialsException} with a custom message.
     *
     * @param message Custom exception's message
     */
    public InvalidCredentialsException(final String message) {
        this(SecurityDefaultError.INVALID_CREDENTIALS.getTitle(), message);
    }

    /**
     * {@link InvalidCredentialsException} with a custom title and message.
     *
     * @param title   Custom exception's title
     * @param message Custom exception's message
     */
    public InvalidCredentialsException(final String title, final String message) {
        super(SecurityDefaultError.INVALID_CREDENTIALS.getType(), title, message);
    }
}