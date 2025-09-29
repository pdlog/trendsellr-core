package com.trendsellr.application.usecase;

import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use Case for finding all products.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllProductsUseCase {

    private final ProductRepository productRepository;

    public List<Product> dispatch() {
        log.info("Find All Products");
        
        return productRepository.findAll();
    }
}