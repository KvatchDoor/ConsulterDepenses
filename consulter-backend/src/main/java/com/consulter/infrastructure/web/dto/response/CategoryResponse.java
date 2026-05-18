package com.consulter.infrastructure.web.dto.response;

import com.consulter.domain.model.Category;
import java.util.UUID;

public record CategoryResponse(UUID id, String label, String color, String icon) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.id(), category.label(), category.color(), category.icon());
    }
}
