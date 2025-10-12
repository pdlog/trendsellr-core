package com.trendsellr.infrastructure.input.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendsellr.domain.exception.error.DefaultGenericError;
import com.trendsellr.domain.exception.error.SecurityDefaultError;
import com.trendsellr.infrastructure.input.rest.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityErrorResponseWriterTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SecurityErrorResponseWriter responseWriter;

    @Test
    @DisplayName("Given response details, when writeErrorResponse is called, then should set status, content type and write JSON body")
    void givenResponseDetails_whenWriteErrorResponse_thenShouldWriteCorrectResponse() throws IOException {
        // Given
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printWriter = mock(PrintWriter.class);
        final HttpStatus status = HttpStatus.FORBIDDEN;
        final DefaultGenericError error = SecurityDefaultError.ACCESS_DENIED;
        final String customMessage = "A test error message.";
        final String expectedJson = "{\"type\":\"SECURITY.ACCESS_DENIED\",\"status\":403,\"title\":\"Access Denied\"}";

        when(response.getWriter()).thenReturn(printWriter);
        when(this.objectMapper.writeValueAsString(any(ErrorDTO.class))).thenReturn(expectedJson);

        // When
        this.responseWriter.writeErrorResponse(response, status, error, customMessage);

        // Then
        verify(response).setStatus(status.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response.getWriter()).write(expectedJson);

        final ArgumentCaptor<ErrorDTO> errorDtoCaptor = ArgumentCaptor.forClass(ErrorDTO.class);
        verify(this.objectMapper).writeValueAsString(errorDtoCaptor.capture());

        final ErrorDTO capturedDto = errorDtoCaptor.getValue();
        assertEquals(error.getType(), capturedDto.getType());
        assertEquals(status.value(), capturedDto.getStatus());
        assertEquals(error.getTitle(), capturedDto.getTitle());
        assertEquals(customMessage, capturedDto.getMessage());
    }
}