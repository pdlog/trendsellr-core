package com.trendsellr.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the pagination details for collections.
 */

@Data
@Builder
public class Pagination {

  private Long totalElements;

  private Integer totalPages;

  private Long page;

  private Integer pageSize;
}
