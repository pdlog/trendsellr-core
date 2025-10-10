package com.trendsellr.infrastructure.input.rest.mapper;

import com.trendsellr.domain.model.Product;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import com.trendsellr.infrastructure.input.rest.dto.ProductCreateDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductUpdateDTO;
import com.trendsellr.input.common.rest.mapper.UriMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class, uses = {UriMapper.class})
public interface ProductDTOMapper {

  /**
   * Converts a Product domain model to a ProductDTO.
   *
   * @param product The domain model.
   * @return The DTO.
   */
  ProductDTO toDto(Product product);

  /**
   * Converts a ProductCreateDTO to a Product domain model.
   *
   * @param productCreateDto The DTO.
   * @return The domain model.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "metadata", ignore = true)
  Product toDomain(ProductCreateDTO productCreateDto);

  /**
   * Converts a ProductUpdateDTO to a Product domain model.
   *
   * @param productUpdateDto The DTO.
   * @return The domain model.
   */

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "source", ignore = true)
  @Mapping(target = "metadata", ignore = true)
  Product toDomain(ProductUpdateDTO productUpdateDto);
}