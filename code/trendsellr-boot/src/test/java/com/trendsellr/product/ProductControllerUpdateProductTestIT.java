package com.trendsellr.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendsellr.domain.exception.error.ProductDefaultError;
import com.trendsellr.domain.exception.error.SecurityDefaultError;
import com.trendsellr.domain.model.product.Product;
import com.trendsellr.domain.repository.ProductRepository;
import com.trendsellr.domain.repository.UserRepository;
import com.trendsellr.domain.service.JwtService;
import com.trendsellr.infrastructure.input.rest.dto.ProductUpdateDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductUpdateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerUpdateProductTestIT extends BaseProductControllerTestIT {

    private static final String UPDATE_PRODUCT_URL = "/api/public/v1/products/{id}";

    private ObjectMapper objectMapper;

    private Product existingProduct;

    public ProductControllerUpdateProductTestIT(@Autowired final ProductRepository productRepository, @Autowired final UserRepository userRepository,
                                                @Autowired final PasswordEncoder passwordEncoder, @Autowired final JwtService jwtService, @Autowired final WebApplicationContext context,
                                                @Value("${api.security.api-key}") final String testApiKey) {
        super(productRepository, userRepository, passwordEncoder, jwtService, context, testApiKey);
    }

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        this.objectMapper = new ObjectMapper();
        this.existingProduct = this.productRepository.save(
                Product.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Default Test Product")
                        .source("DefaultSource")
                        .url("http://default.test.com")
                        .build()
        );
    }


    @Test
    @DisplayName("Given an existing product ID and valid data, when PUT /products/{id} is called, then should update product and return 204 No Content")
    void givenExistingProduct_whenUpdate_thenShouldReturn204() throws Exception {
        // Given
        final ProductUpdateDTO updateData = new ProductUpdateDTO().name("Updated Name");
        final ProductUpdateRequestDTO request = new ProductUpdateRequestDTO().product(updateData);

        // When
        this.secureMockMvc.perform(put(UPDATE_PRODUCT_URL, this.existingProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        // Then
        final Product updatedProduct = this.productRepository.findByProductId(
                this.existingProduct.getId()).orElseThrow();
        assertEquals("Updated Name", updatedProduct.getName());
        assertEquals("DefaultSource",
                updatedProduct.getSource());
    }

    @Test
    @DisplayName("Given a non-existing product ID, when PUT /products/{id} is called, then should return 404 Not Found")
    void givenNonExistingProduct_whenUpdate_thenShouldReturn404() throws Exception {
        // Given
        final String nonExistentId = UUID.randomUUID().toString();
        final ProductUpdateDTO updateData = new ProductUpdateDTO().name("Updated Name");
        final ProductUpdateRequestDTO request = new ProductUpdateRequestDTO().product(updateData);

        // When & Then
        this.secureMockMvc.perform(put(UPDATE_PRODUCT_URL, nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is(ProductDefaultError.NOT_FOUND.getType())));
    }

    @Test
    @DisplayName("Given no API Key, when PUT is called, then should return 403 Forbidden")
    void givenNoApiKey_whenUpdate_thenShouldReturn403() throws Exception {
        final ProductUpdateDTO updateData = new ProductUpdateDTO().name("Updated Name");
        final ProductUpdateRequestDTO request = new ProductUpdateRequestDTO().product(updateData);

        this.insecureMockMvc.perform(put(UPDATE_PRODUCT_URL, this.existingProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is(SecurityDefaultError.ACCESS_DENIED.getType())));
    }

    @Test
    @DisplayName("Given an invalid API Key, when PUT is called, then should return 403 Forbidden")
    void givenInvalidApiKey_whenUpdate_thenShouldReturn403() throws Exception {
        // Given
        final ProductUpdateDTO updateData = new ProductUpdateDTO().name("Updated Name");
        final ProductUpdateRequestDTO request = new ProductUpdateRequestDTO().product(updateData);

        this.insecureMockMvc.perform(put(UPDATE_PRODUCT_URL, this.existingProduct.getId())
                        .header("X-API-KEY", "this-is-an-invalid-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is(SecurityDefaultError.ACCESS_DENIED.getType())));
    }
}