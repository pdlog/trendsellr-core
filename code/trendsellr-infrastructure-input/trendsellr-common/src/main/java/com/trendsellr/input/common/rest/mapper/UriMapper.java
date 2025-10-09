package com.trendsellr.input.common.rest.mapper;

import com.trendsellr.input.common.rest.mapper.config.CommonMapperConfig;
import java.net.URI;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class)
public interface UriMapper {

  default String map(URI uri) {
    return uri.toString();
  }

  default URI map(String url) {
    return URI.create(url);
  }
}
