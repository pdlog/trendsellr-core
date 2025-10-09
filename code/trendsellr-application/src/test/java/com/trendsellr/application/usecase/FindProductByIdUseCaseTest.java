package com.trendsellr.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trendsellr.domain.exception.ProductNotFoundException;
import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindProductByIdUseCaseTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private FindProductByIdUseCase findProductByIdUseCase;

  @Test
  @DisplayName("Given an existing product ID, when dispatch is called, then should return the product")
  void givenExistingId_whenDispatch_thenShouldReturnProduct() {
    // Given
    final String productId = UUID.randomUUID().toString();
    final Product expectedProduct = Product.builder().id(productId).name("Test Product").build();

    when(this.productRepository.findByProductId(productId)).thenReturn(
        Optional.of(expectedProduct));

    // When
    final Product result = this.findProductByIdUseCase.dispatch(productId);

    // Then
    assertNotNull(result);
    assertEquals(expectedProduct, result);
    verify(this.productRepository).findByProductId(productId);
  }

  @Test
  @DisplayName("Given a non-existing product ID, when dispatch is called, then should throw ProductNotFoundException")
  void givenNonExistingId_whenDispatch_thenShouldThrowProductNotFoundException() {
    // Given
    final String nonExistentId = UUID.randomUUID().toString();

    when(this.productRepository.findByProductId(nonExistentId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ProductNotFoundException.class,
        () -> this.findProductByIdUseCase.dispatch(nonExistentId));

    verify(this.productRepository).findByProductId(nonExistentId);
  }
}