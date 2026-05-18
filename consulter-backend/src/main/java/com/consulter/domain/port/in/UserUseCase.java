package com.consulter.domain.port.in;

import com.consulter.domain.model.User;
import java.util.UUID;

public interface UserUseCase {
    User findById(UUID id);
}
