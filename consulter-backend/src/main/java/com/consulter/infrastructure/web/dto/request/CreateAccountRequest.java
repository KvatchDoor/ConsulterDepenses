package com.consulter.infrastructure.web.dto.request;

import java.util.UUID;

public record CreateAccountRequest(UUID ownerId, String name, String currency) {}
