package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.product;

import com.trendsellr.domain.model.Pagination;
import com.trendsellr.domain.model.product.ProductCollection;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.ProductEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.PaginationRepositoryMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = CommonMapperConfig.class, uses = {ProductRepositoryMapper.class,
        PaginationRepositoryMapper.class})
public interface ProductCollectionRepositoryMapper {

    ProductCollection toDomain(final List<ProductEntity> products, final Pagination pagination);
}
