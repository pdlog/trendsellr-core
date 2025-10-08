package com.trendsellr.application.usecase;

import com.trendsellr.domain.exception.ProductNotFoundException;
import com.trendsellr.domain.model.Metadata;
import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Use Case for finding a product by its ID.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateProductUseCase {

  private final ProductRepository productRepository;

  public void dispatch(String id, Product updatedProduct) {
    log.info("Updating Product with id '{}'", id);

    updatedProduct.setMetadata(Metadata.builder()
        .creationDateTime(null)
        .build());

    productRepository.update(id, updatedProduct)
        .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
  }
}