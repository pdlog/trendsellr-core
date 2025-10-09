package com.trendsellr.product;

import com.trendsellr.BaseTestIT;
import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseProductControllerTestIT extends BaseTestIT {

  @Autowired
  protected ProductRepository productRepository;

  protected Product existingProduct;

  @BeforeEach
  void setUp() {
    this.productRepository.deleteAll();
    this.existingProduct = this.productRepository.save(
        Product.builder()
            .id(UUID.randomUUID().toString())
            .name("Default Test Product")
            .source("DefaultSource")
            .url("http://default.test.com")
            .build()
    );
  }
}
