package com.trendsellr.input.common.rest.mapper;

import com.trendsellr.input.common.rest.mapper.config.CommonMapperConfig;
import org.mapstruct.Mapper;

import java.net.URI;

@Mapper(config = CommonMapperConfig.class)
public interface UriMapper {

    default String map(URI uri) {
        return uri.toString();
    }

    default URI map(String url) {
        return URI.create(url);
    }
}
