package ru.sage.contentservice.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileDomain(
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
