package com.trendsellr.infrastructure.input.rest.security;

import com.trendsellr.domain.exception.error.SecurityDefaultError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityErrorResponseWriter responseWriter;

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException {
        log.error("Unauthorized access attempt: {}", authException.getMessage());

        this.responseWriter.writeErrorResponse(
                response,
                HttpStatus.FORBIDDEN,
                SecurityDefaultError.ACCESS_DENIED,
                "A valid API Key is required to access this resource."
        );
    }
}