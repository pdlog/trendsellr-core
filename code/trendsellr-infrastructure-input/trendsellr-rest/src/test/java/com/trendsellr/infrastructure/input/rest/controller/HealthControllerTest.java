package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.infrastructure.input.rest.dto.CheckHealth200ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HealthControllerTest {

    private HealthController healthController;

    @BeforeEach
    void setUp() {
        this.healthController = new HealthController();
    }

    @Test
    @DisplayName("Given a call to health endpoint, when checkHealth is called, then should return 200 OK with UP status")
    void givenCall_whenCheckHealth_thenShouldReturnOkWithUpStatus() {
        // When
        final ResponseEntity<CheckHealth200ResponseDTO> response = this.healthController.checkHealth();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus());
    }
}