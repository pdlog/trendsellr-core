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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private AppApiAuthenticationEntryPoint authenticationEntryPoint;

    private ApiKeyAuthFilter apiKeyAuthFilter;

    private final String validApiKey = "test-key";

    private final List<String> publicPaths = List.of("/api/public/v1/health", "/api/public/v1/auth/**");

    @BeforeEach
    void setUp() {
        this.apiKeyAuthFilter = new ApiKeyAuthFilter(this.validApiKey, this.publicPaths, this.authenticationEntryPoint);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Given a public path, when filter is applied, then should continue chain without checking key")
    void givenPublicPath_whenFilter_thenShouldContinueChain() throws ServletException, IOException {
        // Given
        when(this.request.getRequestURI()).thenReturn("/api/public/v1/health");

        // When
        this.apiKeyAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain).doFilter(this.request, this.response);
        verify(this.authenticationEntryPoint, never()).commence(any(), any(), any());
        verify(this.request, never()).getHeader(any());
    }

    @Test
    @DisplayName("Given a valid API Key on a private path, when filter is applied, then should continue chain")
    void givenValidApiKey_whenFilter_thenShouldContinueChain() throws ServletException, IOException {
        // Given
        when(this.request.getRequestURI()).thenReturn("/api/public/v1/products");
        when(this.request.getHeader("X-API-KEY")).thenReturn(this.validApiKey);

        // When
        this.apiKeyAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain).doFilter(this.request, this.response);
        verify(this.authenticationEntryPoint, never()).commence(any(), any(), any());
    }

    @Test
    @DisplayName("Given an invalid API Key on a private path, when filter is applied, then should call entry point")
    void givenInvalidApiKey_whenFilter_thenShouldCallEntryPoint() throws ServletException, IOException {
        // Given
        when(this.request.getRequestURI()).thenReturn("/api/public/v1/products");
        when(this.request.getHeader("X-API-KEY")).thenReturn("invalid-key");

        // When
        this.apiKeyAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.authenticationEntryPoint).commence(eq(this.request), eq(this.response), any(BadCredentialsException.class));
        verify(this.filterChain, never()).doFilter(this.request, this.response);
    }
}