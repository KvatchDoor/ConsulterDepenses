package com.consulter.domain.port.in;

import com.consulter.domain.model.Movement;
import com.consulter.domain.model.MovementType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MovementUseCase {
    Movement create(UUID accountId, UUID createdBy, UUID categoryId, MovementType type, BigDecimal amount, String description, LocalDate movementDate);
    Movement findById(UUID id);
    List<Movement> findByAccountId(UUID accountId);
    void delete(UUID id);
}
