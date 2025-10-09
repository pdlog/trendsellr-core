package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper;

import com.trendsellr.domain.model.Pagination;
import com.trendsellr.domain.model.ProductCollection;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.ProductEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.config.RepositoryMapperConfig;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = RepositoryMapperConfig.class, uses = {ProductRepositoryMapper.class,
    PaginationRepositoryMapper.class})
public interface ProductCollectionRepositoryMapper {

  ProductCollection toDomain(final List<ProductEntity> products, final Pagination pagination);
}
