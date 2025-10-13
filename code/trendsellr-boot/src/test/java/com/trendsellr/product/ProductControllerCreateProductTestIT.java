package com.trendsellr.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendsellr.BaseTestIT;
import com.trendsellr.domain.exception.error.SecurityDefaultError;
import com.trendsellr.domain.repository.ProductRepository;
import com.trendsellr.infrastructure.input.rest.dto.ProductCreateDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductCreateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ProductControllerCreateProductTestIT extends BaseTestIT {

    private static final String POST_CREATE_PRODUCT = "/api/public/v1/products";

    private final WebApplicationContext context;

    private MockMvc insecureMockMvc;

    private final MockMvc secureMockMvc;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.insecureMockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Given a valid product create request, when POST /products is called, then should create product and return 201 Created")
    void givenValidCreateRequest_whenPostProducts_thenShouldCreateProductAndReturn201()
            throws Exception {
        // Given
        final ProductCreateDTO productData = new ProductCreateDTO()
                .name("Test Integration Product")
                .url(URI.create("http://integration.test.com"))
                .source("IntegrationTest");
        final ProductCreateRequestDTO request = new ProductCreateRequestDTO().product(productData);

        // When & Then
        this.secureMockMvc.perform(post(POST_CREATE_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));

        assertEquals(1, this.productRepository.findAll(0L, 10).getProducts().size());
        assertTrue(this.productRepository.findAll(0L, 10).getProducts().stream()
                .anyMatch(p -> p.getName().equals("Test Integration Product")));
    }

    @Test
    @DisplayName("Given a request without API Key, when POST is called, then should return 403 Forbidden")
    void givenNoApiKey_whenPostProducts_thenShouldReturn403() throws Exception {
        // Given
        final ProductCreateDTO productData = new ProductCreateDTO().name("Test").url(URI.create("http://test.com")).source("Test");
        final ProductCreateRequestDTO request = new ProductCreateRequestDTO().product(productData);

        // When & Then
        this.insecureMockMvc.perform(post(POST_CREATE_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is(SecurityDefaultError.ACCESS_DENIED.getType())));
    }

    @Test
    @DisplayName("Given a request with an invalid API Key, when POST is called, then should return 403 Forbidden")
    void givenInvalidApiKey_whenPostProducts_thenShouldReturn403() throws Exception {
        // Given
        final ProductCreateDTO productData = new ProductCreateDTO().name("Test").url(URI.create("http://test.com")).source("Test");
        final ProductCreateRequestDTO request = new ProductCreateRequestDTO().product(productData);

        // When & Then
        this.insecureMockMvc.perform(post(POST_CREATE_PRODUCT)
                        .header("X-API-KEY", "invalid-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}