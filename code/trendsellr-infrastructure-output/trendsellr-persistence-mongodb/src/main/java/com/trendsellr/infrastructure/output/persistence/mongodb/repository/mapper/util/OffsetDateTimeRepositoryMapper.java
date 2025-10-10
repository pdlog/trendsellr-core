package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.util;

import com.trendsellr.domain.constants.DomainConstants;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class)
public interface OffsetDateTimeRepositoryMapper {

  static OffsetDateTime fromString(final String dateTime) {
    return Objects.nonNull(dateTime) ? OffsetDateTime.parse(dateTime) : null;
  }

  static String toString(final OffsetDateTime dateTime) {
    return Objects.nonNull(dateTime) ? dateTime.format(DomainConstants.DATE_TIME_FORMAT) : null;
  }
}
