package com.consulter.application.service;

import com.consulter.domain.model.Category;
import com.consulter.domain.port.in.CategoryUseCase;
import com.consulter.domain.port.out.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
