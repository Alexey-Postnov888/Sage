package ru.sage.contentservice.presentation.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteResponse(
        UUID id,
        String userId,
        UUID projectId,
        String title,
        String text,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) { }
