package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.application.usecase.CreateProductUseCase;
import com.trendsellr.application.usecase.FindAllProductsUseCase;
import com.trendsellr.application.usecase.FindProductByIdUseCase;
import com.trendsellr.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final FindAllProductsUseCase findAllProductsUseCase;
    private final FindProductByIdUseCase findProductByIdUseCase;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("REST request to create Product: {}", product);
        Product createdProduct = createProductUseCase.dispatch(product);
        return ResponseEntity
                .created(URI.create("/products/" + createdProduct.getId()))
                .body(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        log.info("REST request to get Product by id: {}", id);
        return findProductByIdUseCase.dispatch(id)
                .map(ResponseEntity::ok) // Si lo encuentra, devuelve 200 OK con el producto
                .orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("REST request to get all Products");
        List<Product> products = findAllProductsUseCase.dispatch();
        return ResponseEntity.ok(products);
    }
}