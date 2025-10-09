package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper;

import com.trendsellr.domain.model.Pagination;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.config.RepositoryMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = RepositoryMapperConfig.class)
public interface PaginationRepositoryMapper {

  Pagination toDomain(final Long page, final Integer pageSize, final Integer totalPages,
      final Long totalElements);
}
