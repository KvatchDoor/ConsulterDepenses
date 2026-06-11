package com.consulter.infrastructure.web.controller;

import com.consulter.domain.port.in.CategoryUseCase;
import com.consulter.infrastructure.web.api.CategoriesApi;
import com.consulter.infrastructure.web.model.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoriesApi {

    private final CategoryUseCase categoryUseCase;

    @Override
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryUseCase.findAll().stream()
            .map(c -> new CategoryResponse()
                .id(c.id())
                .label(c.label())
                .color(c.color()))
            .toList());
    }
}
