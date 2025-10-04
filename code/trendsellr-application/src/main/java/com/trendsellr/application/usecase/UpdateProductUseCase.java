package com.trendsellr.application.usecase;

import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

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

        productRepository.update(id, updatedProduct)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + " not found"));
    }
}