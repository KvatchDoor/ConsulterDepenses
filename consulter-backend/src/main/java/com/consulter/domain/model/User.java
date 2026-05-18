package com.consulter.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(
    UUID id,
    String email,
    String lastName,
    String firstName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
