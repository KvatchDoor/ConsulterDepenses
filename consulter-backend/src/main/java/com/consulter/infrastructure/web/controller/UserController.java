package com.consulter.infrastructure.web.controller;

import com.consulter.domain.port.in.UserUseCase;
import com.consulter.infrastructure.web.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return UserResponse.from(userUseCase.findById(id));
    }
}
