package com.trendsellr.infrastructure.input.rest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyAuthFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private AppAuthenticationEntryPoint authenticationEntryPoint;

    private ApiKeyAuthFilter apiKeyAuthFilter;

    private final String validApiKey = "test-key";

    @BeforeEach
    void setUp() {
        this.apiKeyAuthFilter = new ApiKeyAuthFilter(this.validApiKey, this.authenticationEntryPoint);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Given a valid API Key, when filter is applied, then should set authentication and continue chain")
    void givenValidApiKey_whenFilter_thenShouldSetAuthentication() throws ServletException, IOException {
        // Given
        when(this.request.getHeader("X-API-KEY")).thenReturn(validApiKey);

        // When
        this.apiKeyAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain).doFilter(this.request, this.response);
        verify(this.authenticationEntryPoint, never()).commence(any(), any(), any());
    }

    @Test
    @DisplayName("Given an invalid API Key, when filter is applied, then should call entry point and stop chain")
    void givenInvalidApiKey_whenFilter_thenShouldCallEntryPoint() throws ServletException, IOException {
        // Given
        when(this.request.getHeader("X-API-KEY")).thenReturn("invalid-key");

        // When
        this.apiKeyAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.authenticationEntryPoint).commence(eq(this.request), eq(this.response), any(BadCredentialsException.class));
        verify(this.filterChain, never()).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("Given no API Key, when filter is applied, then should call entry point and stop chain")
    void givenNoApiKey_whenFilter_thenShouldCallEntryPoint() throws ServletException, IOException {
        // Given
        when(this.request.getHeader("X-API-KEY")).thenReturn(null);

        // When
        this.apiKeyAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.authenticationEntryPoint).commence(eq(this.request), eq(this.response), any(BadCredentialsException.class));
        verify(this.filterChain, never()).doFilter(this.request, this.response);
    }
}