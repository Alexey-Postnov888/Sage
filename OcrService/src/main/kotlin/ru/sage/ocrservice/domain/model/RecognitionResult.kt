package ru.sage.ocrservice.domain.model

import java.util.UUID

data class RecognitionResult(
    val id: UUID? = null,
    val noteId: UUID,
    val fileId: UUID,
    val text: String?
)