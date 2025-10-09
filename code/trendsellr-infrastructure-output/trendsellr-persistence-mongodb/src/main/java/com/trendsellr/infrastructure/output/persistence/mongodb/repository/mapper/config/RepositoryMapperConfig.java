package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.config;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;

@MapperConfig(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface RepositoryMapperConfig {

}
