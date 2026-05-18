package com.consulter.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Account(
    UUID id,
    UUID ownerId,
    String name,
    BigDecimal balance,
    String currency,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
