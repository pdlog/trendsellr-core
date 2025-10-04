package com.trendsellr.application.usecase;

import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Use Case for finding a product by its ID.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FindProductByIdUseCase {

    private final ProductRepository productRepository;

    public Optional<Product> dispatch(String id) {
        log.info("Find Product by id '{}'", id);

        return productRepository.findByProductId(id);
    }
}