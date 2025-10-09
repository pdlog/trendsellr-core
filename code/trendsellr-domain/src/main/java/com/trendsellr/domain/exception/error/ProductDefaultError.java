package com.trendsellr.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductDefaultError implements DefaultGenericError {

  NOT_FOUND("PRODUCT.NOT_FOUND", "Product Not Found",
      "The product you are looking for does not exist.");

  private final String type;

  private final String title;

  private final String message;
}
