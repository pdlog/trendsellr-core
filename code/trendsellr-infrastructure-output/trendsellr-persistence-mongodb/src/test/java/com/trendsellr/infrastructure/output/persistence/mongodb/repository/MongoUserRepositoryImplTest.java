package com.trendsellr.infrastructure.output.persistence.mongodb.repository;

import com.trendsellr.domain.model.user.User;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.UserEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.user.UserRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.spring.SpringUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MongoUserRepositoryImplTest {

    @Mock
    private SpringUserRepository springUserRepository;

    @Mock
    private UserRepositoryMapper userRepositoryMapper;

    @InjectMocks
    private MongoUserRepositoryImpl userRepository;

    @Test
    @DisplayName("Given a User, when save is called, then should map, save and return mapped domain user")
    void givenUser_whenSave_thenShouldMapAndSave() {
        // Given
        final User domainUser = User.builder().email("test@test.com").build();
        final UserEntity entityToSave = new UserEntity();
        final UserEntity savedEntity = new UserEntity();
        final User expectedDomainUser = User.builder().id("123").email("test@test.com").build();

        when(this.userRepositoryMapper.toEntity(domainUser)).thenReturn(entityToSave);
        when(this.springUserRepository.save(entityToSave)).thenReturn(savedEntity);
        when(this.userRepositoryMapper.toDomain(savedEntity)).thenReturn(expectedDomainUser);

        // When
        final User result = this.userRepository.save(domainUser);

        // Then
        assertEquals(expectedDomainUser, result);
        verify(this.userRepositoryMapper).toEntity(domainUser);
        verify(this.springUserRepository).save(entityToSave);
        verify(this.userRepositoryMapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Given an existing email, when findByEmail is called, then should return user")
    void givenExistingEmail_whenFindByEmail_thenShouldReturnUser() {
        // Given
        final String email = "test@test.com";
        final UserEntity foundEntity = new UserEntity();
        final User expectedDomainUser = User.builder().email(email).build();

        when(this.springUserRepository.findByEmail(email)).thenReturn(Optional.of(foundEntity));
        when(this.userRepositoryMapper.toDomain(foundEntity)).thenReturn(expectedDomainUser);

        // When
        final Optional<User> result = this.userRepository.findByEmail(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedDomainUser, result.get());
        verify(this.springUserRepository).findByEmail(email);
        verify(this.userRepositoryMapper).toDomain(foundEntity);
    }

    @Test
    @DisplayName("Given a non-existing email, when findByEmail is called, then should return empty")
    void givenNonExistingEmail_whenFindByEmail_thenShouldReturnEmpty() {
        // Given
        final String email = "nonexistent@test.com";
        when(this.springUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        final Optional<User> result = this.userRepository.findByEmail(email);

        // Then
        assertFalse(result.isPresent());
        verify(this.springUserRepository).findByEmail(email);
    }

    @Test
    @DisplayName("When deleteAll is called, then should call deleteAll on Spring repository")
    void givenCallToDeleteAll_whenDeleteAll_thenShouldCallSpringRepository() {
        // When
        this.userRepository.deleteAll();

        // Then
        verify(this.springUserRepository).deleteAll();
    }
}