package com.trendsellr.domain.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a collection of products along with pagination details.
 */

@Data
@Builder
public class ProductCollection {

  private List<Product> products;

  private Pagination pagination;
}
