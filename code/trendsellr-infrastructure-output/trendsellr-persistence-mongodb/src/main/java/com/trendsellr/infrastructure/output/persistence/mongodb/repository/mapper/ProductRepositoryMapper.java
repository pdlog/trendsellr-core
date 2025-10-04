package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper;

import com.trendsellr.domain.model.Product;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.ProductEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.config.RepositoryMapperConfig;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.util.OffsetDateTimeRepositoryMapper;
import org.mapstruct.*;

@Mapper(config = RepositoryMapperConfig.class, uses = {OffsetDateTimeRepositoryMapper.class})
public interface ProductRepositoryMapper {

    @Mapping(target = "productId", source = "id")
    ProductEntity toEntity(final Product product);

    @Mapping(target = "id", source = "productId")
    Product toDomain(final ProductEntity productEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(ProductEntity source, @MappingTarget ProductEntity target);
}
