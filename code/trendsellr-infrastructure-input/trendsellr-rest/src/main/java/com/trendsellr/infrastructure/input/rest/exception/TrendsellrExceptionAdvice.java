package com.trendsellr.infrastructure.input.rest.exception;

import com.trendsellr.domain.exception.GenericException;
import com.trendsellr.domain.exception.InvalidCredentialsException;
import com.trendsellr.domain.exception.ProductNotFoundException;
import com.trendsellr.domain.exception.UserAlreadyExistsException;
import com.trendsellr.infrastructure.input.rest.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@ControllerAdvice(basePackages = "com.trendsellr.infrastructure.input.rest.controller")
public class TrendsellrExceptionAdvice {

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException(final GenericException ex) {
        log.error(ex.getMessage(), ex);

        final ErrorDTO error = this.createErrorDto(ex.getType(),
                HttpStatus.BAD_REQUEST, ex.getTitle(), ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFoundException(
            final ProductNotFoundException ex) {
        log.error(ex.getMessage(), ex);

        final ErrorDTO error = this.createErrorDto(ex.getType(), HttpStatus.NOT_FOUND, ex.getTitle(),
                ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        log.error("Conflict: {}", ex.getMessage());
        final ErrorDTO error = this.createErrorDto(ex.getType(), HttpStatus.CONFLICT, ex.getTitle(), ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleInvalidCredentialsException(final InvalidCredentialsException ex) {
        log.error("Authentication failed: {}", ex.getMessage());
        final ErrorDTO error = this.createErrorDto(ex.getType(), HttpStatus.UNAUTHORIZED, ex.getTitle(), ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableException(final GenericException ex) {
        log.error(ex.getMessage(), ex);

        final ErrorDTO error = this.createErrorDto("MALFORMED_JSON_REQUEST",
                HttpStatus.BAD_REQUEST, "Malformed JSON request", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
        log.error(ex.getMessage(), ex);
        final ErrorDTO error = this.createErrorDto("INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

    private ErrorDTO createErrorDto(String type, HttpStatus httpStatus, String title, String detail) {
        return new ErrorDTO()
                .type(type)
                .status(httpStatus.value())
                .title(title)
                .message(detail)
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC));
    }
}
