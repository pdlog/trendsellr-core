package com.trendsellr.domain.exception;

import com.trendsellr.domain.exception.error.ProductDefaultError;

/**
 * Exception thrown when a product is not found.
 */
public class ProductNotFoundException extends GenericException {

  /**
   * {@link ProductDefaultError} is used as default error.
   */
  public ProductNotFoundException() {
    this(ProductDefaultError.NOT_FOUND.getMessage());
  }

  /**
   * {@link ProductNotFoundException} with a custom message.
   *
   * @param message Custom exception's message
   */
  public ProductNotFoundException(String message) {
    this(ProductDefaultError.NOT_FOUND.getTitle(), message);
  }

  /**
   * {@link ProductNotFoundException} with a custom and title message.
   *
   * @param title   Custom exception's title
   * @param message Custom exception's message
   */
  public ProductNotFoundException(String title, String message) {
    super(ProductDefaultError.NOT_FOUND.getType(), title, message);
  }

  /**
   * {@link ProductNotFoundException} with a custom cause.
   *
   * @param title     Custom exception's title
   * @param message   Custom exception's message
   * @param throwable Cause of the exception
   */
  public ProductNotFoundException(String title, String message, Throwable throwable) {
    super(ProductDefaultError.NOT_FOUND.getType(), title, message, throwable);
  }
}
