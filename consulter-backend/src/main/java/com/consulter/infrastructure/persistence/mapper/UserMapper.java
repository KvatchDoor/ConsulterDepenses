package com.consulter.infrastructure.persistence.mapper;

import com.consulter.domain.model.User;
import com.consulter.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserEntity entity);
}
