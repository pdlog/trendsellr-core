package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper;

import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.MetadataEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = CommonMapperConfig.class)
public interface MetadataRepositoryMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateMetadata(MetadataEntity source, @MappingTarget MetadataEntity target);
}
