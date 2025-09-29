package com.trendsellr.domain.repository;

import com.trendsellr.domain.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * Defines the contract that infrastructure adapters must implement.
 */
public interface ProductRepository {

    /**
     * Saves a product (either creates a new one or updates an existing one).
     *
     * @param product The product to save.
     * @return The saved product.
     */
    Product save(Product product);

    /**
     * Finds a product by its unique ID.
     *
     * @param id The ID of the product.
     * @return An Optional containing the product if found, or empty otherwise.
     */
    Optional<Product> findById(String id);

    /**
     * Retrieves all products.
     *
     * @return A list of all products.
     */
    List<Product> findAll();
}