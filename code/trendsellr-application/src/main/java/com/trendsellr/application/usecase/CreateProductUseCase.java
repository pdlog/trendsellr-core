package com.trendsellr.application.usecase;

import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Use Case for creating a new product.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    /**
     * @param product The product to be created.
     * @return The created product.
     */
    public Product dispatch(Product product) {
        log.info("Create Product with name '{}'", product.getName());

        product.setId(UUID.randomUUID().toString());

        return productRepository.save(product);
    }
}