package com.consulter.infrastructure.persistence.adapter;

import com.consulter.domain.model.Movement;
import com.consulter.domain.port.out.MovementRepository;
import com.consulter.infrastructure.persistence.jpa.MovementJpaRepository;
import com.consulter.infrastructure.persistence.mapper.MovementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepository {

    private final MovementJpaRepository jpaRepository;
    private final MovementMapper mapper;

    @Override
    public Movement save(Movement movement) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(movement)));
    }

    @Override
    public Optional<Movement> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Movement> findByAccountId(UUID accountId) {
        return jpaRepository.findByAccountId(accountId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
