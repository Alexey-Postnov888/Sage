package ru.sage.contentservice.presentation.model;

import java.util.UUID;

public record NoteRequest(
        String userId,
        UUID projectId,
        String title,
        String text) { }
