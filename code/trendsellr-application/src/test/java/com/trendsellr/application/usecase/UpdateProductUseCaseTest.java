package com.trendsellr.application.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateProductUseCaseTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private UpdateProductUseCase updateProductUseCase;

  @Test
  @DisplayName("Given an existing ID and product data, when dispatch is called, then should call repository update")
  void givenExistingIdAndProductData_whenDispatch_thenShouldCallRepositoryUpdate() {
    // Given
    final String productId = UUID.randomUUID().toString();
    final Product productToUpdate = Product.builder().name("Updated Name").build();
    final Product updatedProductFromRepo = Product.builder().id(productId).name("Updated Name")
        .build();

    when(this.productRepository.update(eq(productId), any(Product.class))).thenReturn(
        Optional.of(updatedProductFromRepo));

    // When
    this.updateProductUseCase.dispatch(productId, productToUpdate);

    // Then
    final ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(this.productRepository).update(eq(productId), productCaptor.capture());

    final Product capturedProduct = productCaptor.getValue();
    assertNotNull(capturedProduct.getMetadata());
    assertNull(capturedProduct.getMetadata().getCreationDateTime());
  }

  @Test
  @DisplayName("Given a non-existing product ID, when dispatch is called, then should throw ProductNotFoundException")
  void givenNonExistingId_whenDispatch_thenShouldThrowProductNotFoundException() {
    // Given
    final String nonExistentId = UUID.randomUUID().toString();
    final Product productToUpdate = Product.builder().name("Updated Name").build();

    when(this.productRepository.update(nonExistentId, productToUpdate)).thenReturn(
        Optional.empty());

    // When & Then
    assertThrows(ProductNotFoundException.class, () ->
        this.updateProductUseCase.dispatch(nonExistentId, productToUpdate)
    );

    verify(this.productRepository).update(nonExistentId, productToUpdate);
  }
}