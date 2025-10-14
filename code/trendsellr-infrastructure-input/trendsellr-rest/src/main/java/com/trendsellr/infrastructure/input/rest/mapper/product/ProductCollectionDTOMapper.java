package com.trendsellr.infrastructure.input.rest.mapper.product;

import com.trendsellr.domain.model.product.ProductCollection;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import com.trendsellr.infrastructure.input.rest.dto.ProductCollectionDTO;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class, uses = {ProductDTOMapper.class})
public interface ProductCollectionDTOMapper {

    ProductCollectionDTO toDto(ProductCollection productCollection);
}
