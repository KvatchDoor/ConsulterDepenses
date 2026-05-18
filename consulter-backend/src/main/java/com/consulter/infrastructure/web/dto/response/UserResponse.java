package com.consulter.infrastructure.web.dto.response;

import com.consulter.domain.model.User;
import java.util.UUID;

public record UserResponse(UUID id, String email, String lastName, String firstName) {

    public static UserResponse from(User user) {
        return new UserResponse(user.id(), user.email(), user.lastName(), user.firstName());
    }
}
