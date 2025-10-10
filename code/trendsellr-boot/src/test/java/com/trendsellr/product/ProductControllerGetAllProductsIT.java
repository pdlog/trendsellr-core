package com.trendsellr.product;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.trendsellr.BaseTestIT;
import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ProductControllerGetAllProductsIT extends BaseTestIT {

  private static final String GET_ALL_PRODUCTS_URL = "/api/public/v1/products";

  private final MockMvc mockMvc;

  private final ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    this.productRepository.deleteAll();
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
    this.mockMvc.perform(get(GET_ALL_PRODUCTS_URL).param("page", "0").param("pageSize", "3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.products", hasSize(3)))
        .andExpect(jsonPath("$.pagination.totalElements", is(5)))
        .andExpect(jsonPath("$.pagination.totalPages", is(2)))
        .andExpect(jsonPath("$.pagination.page", is(0)))
        .andExpect(jsonPath("$.pagination.pageSize", is(3)));
  }
}
