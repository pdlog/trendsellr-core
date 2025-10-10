package com.trendsellr.product;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendsellr.BaseTestIT;
import com.trendsellr.domain.repository.ProductRepository;
import com.trendsellr.infrastructure.input.rest.dto.ProductCreateDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductCreateRequestDTO;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ProductControllerCreateProductTestIT extends BaseTestIT {

  private static final String POST_CREATE_PRODUCT = "/api/public/v1/products";

  private final MockMvc mockMvc;

  private final ProductRepository productRepository;

  private final ObjectMapper objectMapper;

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
    this.mockMvc.perform(post(POST_CREATE_PRODUCT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()));

    assertEquals(1, this.productRepository.findAll(0L, 10).getProducts().size());
    assertTrue(this.productRepository.findAll(0L, 10).getProducts().stream()
        .anyMatch(p -> p.getName().equals("Test Integration Product")));
  }
}