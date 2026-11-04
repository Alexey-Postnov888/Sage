package ru.sage.contentservice.presentation.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkspaceResponse(
        UUID id,
        String userId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
