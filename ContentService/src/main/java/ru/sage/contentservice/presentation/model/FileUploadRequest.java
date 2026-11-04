package ru.sage.contentservice.presentation.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record FileUploadRequest(
        UUID noteId,
        String userId,
        MultipartFile file
) {
}
