package com.trendsellr.infrastructure.output.persistence.mongodb.repository;

import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.spring.SpringProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ProductRepository port using Spring Data MongoDB.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoProductRepositoryImpl implements ProductRepository {

    private final SpringProductRepository springProductRepository;

    @Override
    public Product save(Product product) {
        log.info("Saving product with id: {}", product.getId());
        return springProductRepository.save(product);
    }

    @Override
    public Optional<Product> findById(String id) {
        log.info("Finding product by id: {}", id);
        return springProductRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        log.info("Finding all products");
        return springProductRepository.findAll();
    }
}