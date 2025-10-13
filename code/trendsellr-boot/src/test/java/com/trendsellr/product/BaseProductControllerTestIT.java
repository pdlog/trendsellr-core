package com.trendsellr.product;

import com.trendsellr.BaseTestIT;
import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class BaseProductControllerTestIT extends BaseTestIT {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc insecureMockMvc;

    protected Product existingProduct;

    @BeforeEach
    void setUp() {
        this.insecureMockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
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
