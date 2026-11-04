package ru.sage.ocrservice.presentation.model

import java.util.UUID

data class RecognitionResponsePresentation(
    val id: UUID?,
    val noteId: UUID,
    val fileId: UUID,
    val text: String?
)