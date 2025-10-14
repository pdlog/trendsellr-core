package com.trendsellr.infrastructure.input.rest.security;

import com.trendsellr.application.service.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Given a valid JWT, when filter is applied, then should set authentication")
    void givenValidJwt_whenFilter_thenShouldSetAuthentication()
            throws ServletException, IOException {
        // Given
        final String token = "valid-jwt";
        final String userEmail = "user@example.com";
        final UserDetails userDetails = new User(userEmail, "password", Collections.emptyList());

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.getUserIdFromToken(token)).thenReturn(userEmail);
        when(this.userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);

        // When
        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userEmail,
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        verify(this.filterChain).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("Given no Authorization header, when filter is applied, then should continue chain without authentication")
    void givenNoAuthHeader_whenFilter_thenShouldContinueChain()
            throws ServletException, IOException {
        // Given
        when(this.request.getHeader("Authorization")).thenReturn(null);

        // When
        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("Given header without 'Bearer ', when filter is applied, then should continue chain without authentication")
    void givenHeaderWithoutBearer_whenFilter_thenShouldContinueChain()
            throws ServletException, IOException {
        // Given
        when(this.request.getHeader("Authorization")).thenReturn("Invalid token");

        // When
        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("Given an invalid JWT, when filter is applied, then should continue chain without authentication")
    void givenInvalidJwt_whenFilter_thenShouldContinueChain() throws ServletException, IOException {
        // Given
        final String invalidToken = "invalid-jwt";
        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(this.jwtService.getUserIdFromToken(invalidToken)).thenThrow(
                new RuntimeException("Invalid token"));

        // When
        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.userDetailsService, never()).loadUserByUsername(any());
        verify(this.filterChain).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("Given a valid JWT but user already authenticated, when filter is applied, then should do nothing")
    void givenValidJwtButUserAlreadyAuthenticated_whenFilter_thenShouldDoNothing()
            throws ServletException, IOException {
        // Given
        final String token = "valid-jwt";
        final String userEmail = "user@example.com";
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userEmail, null));

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.getUserIdFromToken(token)).thenReturn(userEmail);

        // When
        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        verify(this.userDetailsService, never()).loadUserByUsername(any());
        verify(this.filterChain).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("Given a valid JWT for a non-existent user, when filter is applied, then should throw exception and not authenticate")
    void givenValidJwtForNonExistentUser_whenFilter_thenShouldThrowAndNotAuthenticate() throws ServletException, IOException {
        // Given
        final String token = "valid-jwt-for-deleted-user";
        final String userEmail = "deleted@example.com";

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.getUserIdFromToken(token)).thenReturn(userEmail);
        when(this.userDetailsService.loadUserByUsername(userEmail)).thenThrow(new UsernameNotFoundException("User not found"));

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain, never()).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("Given a valid JWT for a non-existent user, when filter is applied, then should throw exception and not authenticate")
    void givenValidJwtForNonExistentUser_whenFilter_thenShouldNotAuthenticate() throws ServletException, IOException {
        // Given
        final String token = "valid-jwt-for-deleted-user";
        final String userEmail = "deleted@example.com";

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.getUserIdFromToken(token)).thenReturn(userEmail);
        when(this.userDetailsService.loadUserByUsername(userEmail))
                .thenThrow(new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"));

        // When
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain, never()).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("Given a valid JWT that returns a null email, when filter is applied, then should continue chain without authentication")
    void givenValidJwtWithNullEmail_whenFilter_thenShouldContinueChain() throws ServletException, IOException {
        // Given
        final String token = "valid-jwt-with-null-email";

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.getUserIdFromToken(token)).thenReturn(null);

        // When
        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.userDetailsService, never()).loadUserByUsername(any());
        verify(this.filterChain).doFilter(this.request, this.response);
    }
}