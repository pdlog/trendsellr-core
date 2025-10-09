package com.trendsellr.product;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.trendsellr.domain.exception.error.ProductDefaultError;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ProductControllerGetByIdTestIT extends BaseProductControllerTestIT {

  private static final String GET_PRODUCT_BY_ID_URL = "/api/public/v1/products/{id}";

  private final MockMvc mockMvc;

  @Test
  @DisplayName("Given an existing product ID, when GET /products/{id} is called, then should return 200 OK with product data")
  void givenExistingProduct_whenGetById_thenShouldReturn200OK() throws Exception {
    // When & Then
    this.mockMvc.perform(get(GET_PRODUCT_BY_ID_URL, this.existingProduct.getId()))
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
    this.mockMvc.perform(get(GET_PRODUCT_BY_ID_URL, nonExistentId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.type", is(ProductDefaultError.NOT_FOUND.getType())))
        .andExpect(jsonPath("$.title", is(ProductDefaultError.NOT_FOUND.getTitle())));
  }
}