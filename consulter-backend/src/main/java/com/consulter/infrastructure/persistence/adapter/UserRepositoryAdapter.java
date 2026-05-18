package com.consulter.infrastructure.persistence.adapter;

import com.consulter.domain.model.User;
import com.consulter.domain.port.out.UserRepository;
import com.consulter.infrastructure.persistence.jpa.UserJpaRepository;
import com.consulter.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}
