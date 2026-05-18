package com.consulter.infrastructure.persistence.adapter;

import com.consulter.domain.model.Category;
import com.consulter.domain.port.out.CategoryRepository;
import com.consulter.infrastructure.persistence.jpa.CategoryJpaRepository;
import com.consulter.infrastructure.persistence.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
