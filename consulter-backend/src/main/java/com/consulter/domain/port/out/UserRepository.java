package com.consulter.domain.port.out;

import com.consulter.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
}
