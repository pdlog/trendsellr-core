package com.trendsellr.application.service;

import com.trendsellr.domain.model.user.Role;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Given an existing user email, when loadUserByUsername is called, then should return UserDetails")
    void givenExistingUser_whenLoadUserByUsername_thenShouldReturnUserDetails() {
        // Given
        final String email = "user@example.com";
        final User domainUser = User.builder()
                .email(email)
                .password("hashed-password")
                .roles(Set.of(Role.USER))
                .build();

        when(this.userRepository.findByEmail(email)).thenReturn(Optional.of(domainUser));

        // When
        final UserDetails result = this.userDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(result);
        assertEquals(domainUser.getEmail(), result.getUsername());
        assertEquals(domainUser.getPassword(), result.getPassword());
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        verify(this.userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Given a non-existing user email, when loadUserByUsername is called, then should throw UsernameNotFoundException")
    void givenNonExistingUser_whenLoadUserByUsername_thenShouldThrowException() {
        // Given
        final String email = "nonexistent@example.com";
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> this.userDetailsService.loadUserByUsername(email));

        verify(this.userRepository).findByEmail(email);
    }
}