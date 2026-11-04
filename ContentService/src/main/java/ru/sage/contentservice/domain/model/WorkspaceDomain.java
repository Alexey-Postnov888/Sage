package ru.sage.contentservice.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkspaceDomain(
        UUID id,
        String userId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
