package com.trendsellr.infrastructure.output.persistence.mongodb.entity;

import com.trendsellr.infrastructure.output.persistence.mongodb.util.MongoEntityConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("ProductEntity")
@Document(collection = MongoEntityConstants.PRODUCTS_DOCUMENT)
public class ProductEntity {

  @Id
  private String id;

  @Indexed(unique = true)
  private String productId;

  private String name;

  private String url;

  private String source;

  private MetadataEntity metadata;
}
