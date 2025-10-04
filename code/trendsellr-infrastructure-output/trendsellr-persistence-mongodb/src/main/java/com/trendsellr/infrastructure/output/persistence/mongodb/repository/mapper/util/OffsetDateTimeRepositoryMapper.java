package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.util;

import com.trendsellr.domain.constants.DomainConstants;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.config.RepositoryMapperConfig;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;

@Mapper(config = RepositoryMapperConfig.class)
public interface OffsetDateTimeRepositoryMapper {

    default String toString(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(DomainConstants.DATE_TIME_FORMAT);
    }

    default OffsetDateTime toOffsetDateTime(final String dateTimeString) {
        return OffsetDateTime.parse(dateTimeString, DomainConstants.DATE_TIME_FORMAT);
    }
}
