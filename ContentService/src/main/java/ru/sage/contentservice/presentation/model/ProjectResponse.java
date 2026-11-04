package ru.sage.contentservice.presentation.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String userId,
        UUID workspaceId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
