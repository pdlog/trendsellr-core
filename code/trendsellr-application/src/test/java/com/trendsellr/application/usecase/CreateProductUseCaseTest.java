package com.trendsellr.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private CreateProductUseCase createProductUseCase;

  @Test
  @DisplayName("Given a product without ID or metadata, when dispatch is called, then it should set them and save")
  void givenNewProduct_whenDispatch_thenShouldSetIdAndMetadataAndSave() {
    // Given
    final Product inputProduct = Product.builder()
        .name("Test Product")
        .url("http://test.com")
        .source("Test")
        .build();

    // When
    when(this.productRepository.save(inputProduct)).thenReturn(inputProduct);

    final Product result = this.createProductUseCase.dispatch(inputProduct);

    // Then

    final ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(this.productRepository).save(productCaptor.capture());

    final Product capturedProduct = productCaptor.getValue();
    assertNotNull(capturedProduct.getId(), "Product ID should not be null after dispatch");
    assertNotNull(capturedProduct.getMetadata(), "Metadata should not be null after dispatch");
    assertEquals(1, capturedProduct.getMetadata().getVersion(), "Metadata version should be 1");

    assertEquals(capturedProduct, result);
  }
}