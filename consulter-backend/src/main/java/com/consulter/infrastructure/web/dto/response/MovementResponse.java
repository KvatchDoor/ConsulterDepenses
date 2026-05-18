package com.consulter.infrastructure.web.dto.response;

import com.consulter.domain.model.Movement;
import com.consulter.domain.model.MovementType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MovementResponse(
    UUID id,
    UUID accountId,
    UUID createdBy,
    UUID categoryId,
    MovementType type,
    BigDecimal amount,
    String description,
    LocalDate movementDate
) {
    public static MovementResponse from(Movement m) {
        return new MovementResponse(m.id(), m.accountId(), m.createdBy(), m.categoryId(), m.type(), m.amount(), m.description(), m.movementDate());
    }
}
