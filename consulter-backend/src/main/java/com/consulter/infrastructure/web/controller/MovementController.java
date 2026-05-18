package com.consulter.infrastructure.web.controller;

import com.consulter.domain.model.Movement;
import com.consulter.domain.port.in.MovementUseCase;
import com.consulter.infrastructure.web.api.MovementsApi;
import com.consulter.infrastructure.web.model.CreateMovementRequest;
import com.consulter.infrastructure.web.model.MovementResponse;
import com.consulter.infrastructure.web.model.MovementType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MovementController implements MovementsApi {

    private final MovementUseCase movementUseCase;

    @Override
    public ResponseEntity<MovementResponse> createMovement(CreateMovementRequest request) {
        Movement movement = movementUseCase.create(
            request.getAccountId(),
            request.getCreatedBy(),
            request.getCategoryId(),
            com.consulter.domain.model.MovementType.valueOf(request.getType().name()),
            request.getAmount(),
            request.getDescription(),
            request.getMovementDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(movement));
    }

    @Override
    public ResponseEntity<List<MovementResponse>> getMovementsByAccount(UUID accountId) {
        return ResponseEntity.ok(movementUseCase.findByAccountId(accountId).stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<MovementResponse> getMovementById(UUID id) {
        return ResponseEntity.ok(toResponse(movementUseCase.findById(id)));
    }

    @Override
    public ResponseEntity<Void> deleteMovement(UUID id) {
        movementUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MovementResponse toResponse(Movement m) {
        return new MovementResponse()
            .id(m.id())
            .accountId(m.accountId())
            .createdBy(m.createdBy())
            .categoryId(m.categoryId())
            .type(MovementType.valueOf(m.type().name()))
            .amount(m.amount())
            .description(m.description())
            .movementDate(m.movementDate());
    }
}
