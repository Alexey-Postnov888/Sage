package ru.sage.contentservice.domain.model;


import java.time.LocalDateTime;
import java.util.UUID;

public record NoteDomain(
        UUID id,
        String userId,
        UUID projectId,
        String title,
        String text,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) { }
