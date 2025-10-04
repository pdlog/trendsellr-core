package com.trendsellr.application.usecase;

import com.trendsellr.domain.model.ProductCollection;
import com.trendsellr.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Use Case for finding all products.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllProductsUseCase {

    private final ProductRepository productRepository;

    public ProductCollection dispatch(final Long page, final Integer pageSize) {
        log.info("Find All Products - page: {}, pageSize: {}", page, pageSize);

        return this.productRepository.findAll(page, pageSize);
    }
}