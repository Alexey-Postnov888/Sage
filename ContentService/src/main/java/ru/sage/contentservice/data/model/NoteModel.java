package ru.sage.contentservice.data.model;

import java.time.LocalDateTime;
import java.util.UUID;


public record NoteModel(UUID uuid, String userId, UUID projectId, String title, String text, LocalDateTime createdAt, LocalDateTime updatedAt) { }

