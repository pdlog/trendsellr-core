package com.trendsellr.domain.model.product;

import com.trendsellr.domain.model.Pagination;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents a collection of products along with pagination details.
 */

@Data
@Builder
public class ProductCollection {

    private List<Product> products;

    private Pagination pagination;
}
