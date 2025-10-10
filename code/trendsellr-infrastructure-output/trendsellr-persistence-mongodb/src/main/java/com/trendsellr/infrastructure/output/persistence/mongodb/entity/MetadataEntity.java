package com.trendsellr.infrastructure.output.persistence.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetadataEntity {

  private String creationDateTime;

  private String lastUpdatedDateTime;

  private Integer version;
}
