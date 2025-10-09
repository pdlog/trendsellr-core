package com.trendsellr.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a product being tracked by the application.
 */

@Data
@Builder
public class Product {

  private String id;
  private String name;
  private String url;
  private String source;
  private Metadata metadata;
}