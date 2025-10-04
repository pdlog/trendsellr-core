package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.config;

import org.mapstruct.*;

@MapperConfig(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface RepositoryMapperConfig {
}
