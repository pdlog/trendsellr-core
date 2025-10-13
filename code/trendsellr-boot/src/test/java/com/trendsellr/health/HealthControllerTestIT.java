package com.trendsellr.health;

import com.trendsellr.BaseTestIT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class HealthControllerTestIT extends BaseTestIT {

    private static final String HEALTH_URL = "/api/public/v1/health";

    private final MockMvc mockMvc;

    @Test
    @DisplayName("Given the application is running, when GET /health is called, then should return 200 OK with UP status")
    void givenAppIsRunning_whenGetHealth_thenShouldReturnOkWithUpStatus() throws Exception {
        // When & Then
        this.mockMvc.perform(get(HEALTH_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }
}