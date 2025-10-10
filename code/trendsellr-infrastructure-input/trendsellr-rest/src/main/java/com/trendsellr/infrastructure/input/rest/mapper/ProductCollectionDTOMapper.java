package com.trendsellr.infrastructure.input.rest.mapper;

import com.trendsellr.domain.model.ProductCollection;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import com.trendsellr.infrastructure.input.rest.dto.ProductCollectionDTO;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class, uses = {ProductDTOMapper.class})
public interface ProductCollectionDTOMapper {

  ProductCollectionDTO toDto(ProductCollection productCollection);
}
