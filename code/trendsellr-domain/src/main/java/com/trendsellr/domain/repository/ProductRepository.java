package com.trendsellr.domain.repository;

import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.model.ProductCollection;
import java.util.Optional;

/**
 * Defines the contract that infrastructure must implement.
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
   * Updates an existing product identified by its unique ID.
   *
   * @param id      The unique ID of the product to update.
   * @param product The product data to update.
   * @return An Optional containing the updated product if found, or empty otherwise.
   */
  Optional<Product> update(String id, Product product);

  /**
   * Finds a product by its unique product ID.
   *
   * @param productId The unique product ID of the product.
   * @return An Optional containing the product if found, or empty otherwise.
   */
  Optional<Product> findByProductId(String productId);

  /**
   * Retrieves all products with pagination support.
   *
   * @param page     The page number to retrieve (0-based).
   * @param pageSize The number of products per page.
   * @return A ProductCollection containing the products and pagination metadata.
   */
  ProductCollection findAll(Long page, Integer pageSize);

  /**
   * Deletes all products from the repository.
   */
  void deleteAll();
}