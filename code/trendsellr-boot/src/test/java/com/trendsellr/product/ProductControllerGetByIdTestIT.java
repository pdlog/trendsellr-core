package com.trendsellr.product;

import com.trendsellr.domain.exception.error.ProductDefaultError;
import com.trendsellr.domain.exception.error.SecurityDefaultError;
import com.trendsellr.domain.model.product.Product;
import com.trendsellr.domain.repository.ProductRepository;
import com.trendsellr.domain.repository.UserRepository;
import com.trendsellr.domain.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerGetByIdTestIT extends BaseProductControllerTestIT {

    private static final String GET_PRODUCT_BY_ID_URL = "/api/public/v1/products/{id}";

    private Product existingProduct;

    protected ProductControllerGetByIdTestIT(@Autowired final ProductRepository productRepository, @Autowired final UserRepository userRepository,
                                             @Autowired final PasswordEncoder passwordEncoder, @Autowired final JwtService jwtService, @Autowired final WebApplicationContext context,
                                             @Value("${api.security.api-key}") final String testApiKey) {
        super(productRepository, userRepository, passwordEncoder, jwtService, context, testApiKey);
    }

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
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
    @DisplayName("Given an existing product ID, when GET /products/{id} is called, then should return 200 OK with product data")
    void givenExistingProduct_whenGetById_thenShouldReturn200OK() throws Exception {
        // When & Then
        this.secureMockMvc.perform(get(GET_PRODUCT_BY_ID_URL, this.existingProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.id", is(this.existingProduct.getId())))
                .andExpect(jsonPath("$.product.name", is("Default Test Product")));
    }

    @Test
    @DisplayName("Given a non-existing product ID, when GET /products/{id} is called, then should return 404 Not Found with error body")
    void givenNonExistingProduct_whenGetById_thenShouldReturn404NotFound() throws Exception {
        // Given
        final String nonExistentId = UUID.randomUUID().toString();

        // When & Then
        this.secureMockMvc.perform(get(GET_PRODUCT_BY_ID_URL, nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.type", is(ProductDefaultError.NOT_FOUND.getType())))
                .andExpect(jsonPath("$.title", is(ProductDefaultError.NOT_FOUND.getTitle())));
    }

    @Test
    @DisplayName("Given no API Key, when GET by ID is called, then should return 403 Forbidden")
    void givenNoApiKey_whenGetById_thenShouldReturn403() throws Exception {
        this.insecureMockMvc.perform(get(GET_PRODUCT_BY_ID_URL, this.existingProduct.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is(SecurityDefaultError.ACCESS_DENIED.getType())));
    }

    @Test
    @DisplayName("Given an invalid API Key, when GET by ID is called, then should return 403 Forbidden")
    void givenInvalidApiKey_whenGetById_thenShouldReturn403() throws Exception {
        // When & Then
        this.insecureMockMvc.perform(get(GET_PRODUCT_BY_ID_URL, this.existingProduct.getId())
                        .header("X-API-KEY", "this-is-an-invalid-key"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is(SecurityDefaultError.ACCESS_DENIED.getType())));
    }
}