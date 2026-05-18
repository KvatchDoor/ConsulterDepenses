package com.consulter.infrastructure.web.controller;

import com.consulter.domain.port.in.CategoryUseCase;
import com.consulter.infrastructure.web.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryUseCase.findAll().stream().map(CategoryResponse::from).toList();
    }
}
