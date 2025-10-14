package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.product;

import com.trendsellr.domain.model.product.Product;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.ProductEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.MetadataRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.util.OffsetDateTimeRepositoryMapper;
import org.mapstruct.*;

@Mapper(config = CommonMapperConfig.class, uses = {MetadataRepositoryMapper.class,
        OffsetDateTimeRepositoryMapper.class})
public interface ProductRepositoryMapper {

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "id", ignore = true)
    ProductEntity toEntity(final Product product);

    @Mapping(target = "id", source = "productId")
    Product toDomain(final ProductEntity productEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(ProductEntity source, @MappingTarget ProductEntity target);
}
