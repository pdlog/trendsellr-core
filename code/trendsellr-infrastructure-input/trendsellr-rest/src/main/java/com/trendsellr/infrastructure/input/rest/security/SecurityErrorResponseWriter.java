package com.trendsellr.infrastructure.input.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendsellr.domain.exception.error.DefaultGenericError;
import com.trendsellr.infrastructure.input.rest.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class SecurityErrorResponseWriter {

    private final ObjectMapper objectMapper;

    public void writeErrorResponse(final HttpServletResponse response, final HttpStatus status,
                                   final DefaultGenericError error, final String customMessage) throws IOException {

        final ErrorDTO errorDTO = new ErrorDTO()
                .type(error.getType())
                .status(status.value())
                .title(error.getTitle())
                .message(customMessage)
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC));

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(this.objectMapper.writeValueAsString(errorDTO));
    }
}