package com.consulter.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Movement(
    UUID id,
    UUID accountId,
    UUID createdBy,
    UUID categoryId,
    MovementType type,
    BigDecimal amount,
    String description,
    LocalDate movementDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
