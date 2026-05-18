package com.consulter.infrastructure.persistence.mapper;

import com.consulter.domain.model.Category;
import com.consulter.infrastructure.persistence.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toDomain(CategoryEntity entity);
}
