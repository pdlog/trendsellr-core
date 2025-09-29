package com.trendsellr.infrastructure.output.persistence.mongodb.repository.spring;

import com.trendsellr.domain.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Product entity.
 */
@Repository
public interface SpringProductRepository extends MongoRepository<Product, String> {}