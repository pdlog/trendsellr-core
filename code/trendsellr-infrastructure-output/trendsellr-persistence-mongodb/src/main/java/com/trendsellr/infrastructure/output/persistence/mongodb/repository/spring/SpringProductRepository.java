package com.trendsellr.infrastructure.output.persistence.mongodb.repository.spring;

import com.trendsellr.infrastructure.output.persistence.mongodb.entity.ProductEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Product entity.
 */
@Repository
public interface SpringProductRepository extends MongoRepository<ProductEntity, String> {

  Optional<ProductEntity> findByProductId(String productId);
}