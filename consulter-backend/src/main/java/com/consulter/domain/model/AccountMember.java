package com.consulter.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountMember(
    UUID accountId,
    UUID userId,
    LocalDateTime joinedAt
) {}
