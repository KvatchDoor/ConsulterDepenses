package com.consulter.infrastructure.web.controller;

import com.consulter.domain.port.in.MovementUseCase;
import com.consulter.infrastructure.web.dto.request.CreateMovementRequest;
import com.consulter.infrastructure.web.dto.response.MovementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementUseCase movementUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovementResponse create(@RequestBody CreateMovementRequest request) {
        return MovementResponse.from(movementUseCase.create(
            request.accountId(), request.createdBy(), request.categoryId(),
            request.type(), request.amount(), request.description(), request.movementDate()
        ));
    }

    @GetMapping
    public List<MovementResponse> findByAccount(@RequestParam UUID accountId) {
        return movementUseCase.findByAccountId(accountId).stream().map(MovementResponse::from).toList();
    }

    @GetMapping("/{id}")
    public MovementResponse findById(@PathVariable UUID id) {
        return MovementResponse.from(movementUseCase.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        movementUseCase.delete(id);
    }
}
