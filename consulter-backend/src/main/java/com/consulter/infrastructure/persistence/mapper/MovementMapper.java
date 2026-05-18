package com.consulter.infrastructure.persistence.mapper;

import com.consulter.domain.model.Movement;
import com.consulter.infrastructure.persistence.entity.MovementEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovementMapper {
    Movement toDomain(MovementEntity entity);
    MovementEntity toEntity(Movement movement);
}
