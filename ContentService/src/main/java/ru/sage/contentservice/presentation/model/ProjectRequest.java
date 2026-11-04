package ru.sage.contentservice.presentation.model;

import java.util.UUID;

public record ProjectRequest(
        String userId,
        UUID workspaceId,
        String title
) {
}
