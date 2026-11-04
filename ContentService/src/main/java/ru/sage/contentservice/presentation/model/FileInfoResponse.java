package ru.sage.contentservice.presentation.model;

import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileInfoResponse(
        UUID id,
        UUID noteId,
        String fid,
        String filename,
        String key,
        String url,
        String extension,
        String mimeType,
        Long size,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
