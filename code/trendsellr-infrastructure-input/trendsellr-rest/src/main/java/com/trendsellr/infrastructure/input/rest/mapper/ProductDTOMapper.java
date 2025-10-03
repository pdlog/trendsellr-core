package com.trendsellr.infrastructure.input.rest.mapper;

import com.trendsellr.domain.model.Product;
import com.trendsellr.infrastructure.input.rest.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDTOMapper {

    /**
     * Converts a Product domain model to a ProductDTO.
     * @param product The domain model.
     * @return The DTO.
     */
    ProductDTO toDto(Product product);

    /**
     * Converts a ProductDTO to a Product domain model.
     * @param productDTO The DTO.
     * @return The domain model.
     */
    Product toDomain(ProductDTO productDTO);
}