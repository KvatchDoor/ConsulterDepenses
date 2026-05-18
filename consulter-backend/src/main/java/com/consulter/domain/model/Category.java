package com.consulter.domain.model;

import java.util.UUID;

public record Category(
    UUID id,
    String label,
    String color,
    String icon
) {}
