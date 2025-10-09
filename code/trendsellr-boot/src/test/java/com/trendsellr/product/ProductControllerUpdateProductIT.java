package com.trendsellr.product;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendsellr.domain.exception.error.ProductDefaultError;
import com.trendsellr.domain.model.Product;
import com.trendsellr.infrastructure.input.rest.dto.ProductUpdateDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductUpdateRequestDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ProductControllerUpdateProductIT extends BaseProductControllerTestIT {

  private static final String UPDATE_PRODUCT_URL = "/api/public/v1/products/{id}";

  private final MockMvc mockMvc;

  private final ObjectMapper objectMapper;

  @Test
  @DisplayName("Given an existing product ID and valid data, when PUT /products/{id} is called, then should update product and return 204 No Content")
  void givenExistingProduct_whenUpdate_thenShouldReturn204() throws Exception {
    // Given
    final ProductUpdateDTO updateData = new ProductUpdateDTO().name("Updated Name");
    final ProductUpdateRequestDTO request = new ProductUpdateRequestDTO().product(updateData);

    // When
    this.mockMvc.perform(put(UPDATE_PRODUCT_URL, this.existingProduct.getId())
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
    this.mockMvc.perform(put(UPDATE_PRODUCT_URL, nonExistentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.type", is(ProductDefaultError.NOT_FOUND.getType())));
  }
}