package com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.user;

import com.trendsellr.domain.model.user.Role;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.infrastructure.common.mapper.CommonMapperConfig;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.UserEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.MetadataRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.util.OffsetDateTimeRepositoryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = CommonMapperConfig.class, uses = {MetadataRepositoryMapper.class, OffsetDateTimeRepositoryMapper.class})
public interface UserRepositoryMapper {

    @Mapping(target = "userId", source = "id")
    UserEntity toEntity(final User user);

    @Mapping(target = "id", source = "userId")
    User toDomain(final UserEntity userEntity);

    default Set<String> mapRolesToStrings(final Set<Role> roles) {
        if (Objects.isNull(roles)) {
            return Collections.emptySet();
        }
        return roles.stream().map(Role::name).collect(Collectors.toSet());
    }

    default Set<Role> mapStringsToRoles(final Set<String> roles) {
        if (Objects.isNull(roles)) {
            return Collections.emptySet();
        }
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }
}