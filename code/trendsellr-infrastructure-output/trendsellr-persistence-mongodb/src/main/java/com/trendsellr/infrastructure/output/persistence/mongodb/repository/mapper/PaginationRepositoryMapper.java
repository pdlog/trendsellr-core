package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper;

import com.trendsellr.domain.model.Pagination;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class)
public interface PaginationRepositoryMapper {

  Pagination toDomain(final Long page, final Integer pageSize, final Integer totalPages,
      final Long totalElements);
}
