package ru.sage.contentservice.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectDomain(
        UUID id,
        String userId,
        UUID workspaceId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
