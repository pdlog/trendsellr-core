package com.trendsellr.infrastructure.input.rest.security;

import com.trendsellr.domain.exception.error.SecurityDefaultError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppUserAuthenticationEntryPointTest {

    @Mock
    private SecurityErrorResponseWriter responseWriter;

    @InjectMocks
    private AppUserAuthenticationEntryPoint appUserAuthenticationEntryPoint;

    @Test
    @DisplayName("Given an authentication failure, when commence is called, then should delegate to SecurityErrorResponseWriter")
    void givenAuthenticationFailure_whenCommence_thenShouldDelegateToResponseWriter() throws IOException {
        // Given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final AuthenticationException authException = mock(AuthenticationException.class);

        final String expectedMessage = "A valid user authentication token (JWT) is required for this resource.";

        // When
        this.appUserAuthenticationEntryPoint.commence(request, response, authException);

        // Then
        verify(this.responseWriter).writeErrorResponse(
                response,
                HttpStatus.UNAUTHORIZED,
                SecurityDefaultError.INVALID_CREDENTIALS,
                expectedMessage
        );
    }
}