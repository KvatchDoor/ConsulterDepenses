package com.consulter.infrastructure.web.controller;

import com.consulter.domain.model.Movement;
import com.consulter.domain.model.MovementType;
import com.consulter.domain.port.in.MovementUseCase;
import com.consulter.infrastructure.web.model.CreateMovementRequest;
import com.consulter.infrastructure.web.model.MovementResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDate.of;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementControllerTest {

    private static final LocalDate FIXED_DATE = of(2024, 1, 15);

    @Mock
    private MovementUseCase movementUseCase;

    @InjectMocks
    private MovementController movementController;

    private Movement sampleMovement(UUID accountId) {
        return new Movement(
            UUID.randomUUID(), accountId, UUID.randomUUID(), null,
            MovementType.CREDIT, new BigDecimal("50.00"), "test", FIXED_DATE, null, null
        );
    }

    @Test
    void createMovement_returnsCreatedStatus() {
        UUID accountId = UUID.randomUUID();
        UUID createdBy = UUID.randomUUID();
        Movement movement = sampleMovement(accountId);

        CreateMovementRequest request = new CreateMovementRequest()
            .accountId(accountId)
            .createdBy(createdBy)
            .type(com.consulter.infrastructure.web.model.MovementType.CREDIT)
            .amount(new BigDecimal("50.00"))
            .movementDate(FIXED_DATE);

        when(movementUseCase.create(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(movement);

        ResponseEntity<MovementResponse> response = movementController.createMovement(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(movement.id());
        assertThat(response.getBody().getAmount()).isEqualByComparingTo(movement.amount());
    }

    @Test
    void getMovementsByAccount_returnsOkWithList() {
        UUID accountId = UUID.randomUUID();
        List<Movement> movements = List.of(sampleMovement(accountId), sampleMovement(accountId));

        when(movementUseCase.findByAccountId(accountId)).thenReturn(movements);

        ResponseEntity<List<MovementResponse>> response = movementController.getMovementsByAccount(accountId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void getMovementById_returnsOk() {
        UUID accountId = UUID.randomUUID();
        Movement movement = sampleMovement(accountId);

        when(movementUseCase.findById(movement.id())).thenReturn(movement);

        ResponseEntity<MovementResponse> response = movementController.getMovementById(movement.id());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(movement.id());
    }

    @Test
    void deleteMovement_returnsNoContent() {
        UUID movementId = UUID.randomUUID();
        doNothing().when(movementUseCase).delete(movementId);

        ResponseEntity<Void> response = movementController.deleteMovement(movementId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(movementUseCase).delete(movementId);
    }
}
