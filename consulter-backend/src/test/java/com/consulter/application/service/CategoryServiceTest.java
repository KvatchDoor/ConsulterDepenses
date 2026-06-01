package com.consulter.application.service;

import com.consulter.domain.model.Category;
import com.consulter.domain.port.out.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findAll_returnsAllCategories() {
        List<Category> categories = List.of(
            new Category(UUID.randomUUID(), "Alimentation", "#FF0000", "food"),
            new Category(UUID.randomUUID(), "Transport", "#00FF00", "car")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        assertThat(categoryService.findAll()).isEqualTo(categories);
    }

    @Test
    void findAll_returnsEmptyList_whenNoCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        assertThat(categoryService.findAll()).isEmpty();
    }
}
