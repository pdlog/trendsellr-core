package com.trendsellr.product;

import com.trendsellr.domain.exception.error.SecurityDefaultError;
import com.trendsellr.domain.model.product.Product;
import com.trendsellr.domain.repository.ProductRepository;
import com.trendsellr.domain.repository.UserRepository;
import com.trendsellr.domain.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerGetAllProductsIT extends BaseProductControllerTestIT {

    private static final String GET_ALL_PRODUCTS_URL = "/api/public/v1/products";

    protected ProductControllerGetAllProductsIT(@Autowired final ProductRepository productRepository, @Autowired final UserRepository userRepository,
                                                @Autowired final PasswordEncoder passwordEncoder, @Autowired final JwtService jwtService, @Autowired final WebApplicationContext context,
                                                @Value("${api.security.api-key}") final String testApiKey) {
        super(productRepository, userRepository, passwordEncoder, jwtService, context, testApiKey);
    }

    @Test
    @DisplayName("Given multiple products exist, when GET /products is called with pagination, then should return paginated result")
    void givenMultipleProducts_whenGetAll_thenShouldReturnPaginatedResult() throws Exception {

        // Given
        IntStream.range(1, 6).forEach(i ->
                this.productRepository.save(Product.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Product " + i)
                        .build())
        );

        // When & Then
        this.secureMockMvc.perform(get(GET_ALL_PRODUCTS_URL).param("page", "0").param("pageSize", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(3)))
                .andExpect(jsonPath("$.pagination.totalElements", is(5)))
                .andExpect(jsonPath("$.pagination.totalPages", is(2)))
                .andExpect(jsonPath("$.pagination.page", is(0)))
                .andExpect(jsonPath("$.pagination.pageSize", is(3)));
    }

    @Test
    @DisplayName("Given no API Key, when GET all is called, then should return 403 Forbidden")
    void givenNoApiKey_whenGetAll_thenShouldReturn403() throws Exception {
        this.insecureMockMvc.perform(get(GET_ALL_PRODUCTS_URL))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is(SecurityDefaultError.ACCESS_DENIED.getType())));
    }

    @Test
    @DisplayName("Given an invalid API Key, when GET all is called, then should return 403 Forbidden")
    void givenInvalidApiKey_whenGetAll_thenShouldReturn403() throws Exception {
        // When & Then
        this.insecureMockMvc.perform(get(GET_ALL_PRODUCTS_URL)
                        .header("X-API-KEY", "this-is-an-invalid-key"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is(SecurityDefaultError.ACCESS_DENIED.getType())));
    }
}
