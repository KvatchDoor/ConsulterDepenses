package com.consulter.domain.port.in;

import com.consulter.domain.model.Category;
import java.util.List;

public interface CategoryUseCase {
    List<Category> findAll();
}
