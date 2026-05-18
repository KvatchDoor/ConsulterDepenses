package com.consulter.domain.port.out;

import com.consulter.domain.model.Movement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovementRepository {
    Movement save(Movement movement);
    Optional<Movement> findById(UUID id);
    List<Movement> findByAccountId(UUID accountId);
    void deleteById(UUID id);
}
