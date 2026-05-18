package com.consulter.domain.port.out;

import com.consulter.domain.model.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
}
