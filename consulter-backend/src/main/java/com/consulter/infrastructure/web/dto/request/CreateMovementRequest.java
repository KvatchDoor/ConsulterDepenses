package com.consulter.infrastructure.web.dto.request;

import com.consulter.domain.model.MovementType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateMovementRequest(
    UUID accountId,
    UUID createdBy,
    UUID categoryId,
    MovementType type,
    BigDecimal amount,
    String description,
    LocalDate movementDate
) {}
