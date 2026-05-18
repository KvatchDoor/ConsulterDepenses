package com.consulter.infrastructure.web.controller;

import com.consulter.domain.model.User;
import com.consulter.domain.port.in.UserUseCase;
import com.consulter.infrastructure.web.api.UsersApi;
import com.consulter.infrastructure.web.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserUseCase userUseCase;

    @Override
    public ResponseEntity<UserResponse> getUserById(UUID id) {
        User user = userUseCase.findById(id);
        return ResponseEntity.ok(new UserResponse()
            .id(user.id())
            .email(user.email())
            .lastName(user.lastName())
            .firstName(user.firstName()));
    }
}
