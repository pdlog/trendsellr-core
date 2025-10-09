package com.trendsellr.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trendsellr.domain.model.Pagination;
import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.model.ProductCollection;
import com.trendsellr.domain.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindAllProductsUseCaseTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private FindAllProductsUseCase findAllProductsUseCase;

  @Test
  @DisplayName("Given page and pageSize, when dispatch is called, then should call repository and return a product collection")
  void givenPageAndPageSize_whenDispatch_thenShouldCallRepositoryAndReturnCollection() {
    // Given
    final Long page = 0L;
    final Integer pageSize = 10;
    final ProductCollection expectedCollection = ProductCollection.builder()
        .products(List.of(Product.builder().id("123").build()))
        .pagination(Pagination.builder().page(page).pageSize(pageSize).build())
        .build();

    when(this.productRepository.findAll(page, pageSize)).thenReturn(expectedCollection);

    // When
    final ProductCollection result = this.findAllProductsUseCase.dispatch(page, pageSize);

    // Then
    assertNotNull(result);
    assertEquals(expectedCollection, result);
    verify(this.productRepository).findAll(page, pageSize);
  }
}