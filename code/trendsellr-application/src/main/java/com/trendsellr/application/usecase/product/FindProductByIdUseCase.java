package com.trendsellr.application.usecase.product;

import com.trendsellr.domain.exception.ProductNotFoundException;
import com.trendsellr.domain.model.product.Product;
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
public class FindProductByIdUseCase {

    private final ProductRepository productRepository;

    public Product dispatch(String id) {
        log.info("Find Product by id '{}'", id);

        return this.productRepository.findByProductId(id).orElseThrow(() ->
                new ProductNotFoundException("Product with id " + id + " not found")
        );
    }
}