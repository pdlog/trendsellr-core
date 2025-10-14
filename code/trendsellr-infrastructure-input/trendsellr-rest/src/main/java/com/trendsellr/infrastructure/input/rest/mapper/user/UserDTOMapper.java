package com.trendsellr.infrastructure.input.rest.mapper.user;

import com.trendsellr.domain.model.user.User;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import com.trendsellr.infrastructure.input.rest.dto.UserRegisterRequestDTO;
import com.trendsellr.infrastructure.input.rest.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface UserDTOMapper {

    /**
     * Converts a registration DTO to a User domain model.
     *
     * @param userRegisterRequestDTO The DTO from the API request.
     * @return The User domain model.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toDomain(final UserRegisterRequestDTO userRegisterRequestDTO);

    /**
     * Converts a User domain model to a response DTO.
     *
     * @param user The User domain model.
     * @return The UserResponseDTO for the API response.
     */
    UserResponseDTO toDto(final User user);
}