package ru.sage.ocrservice.domain.model.request

import java.util.UUID

data class GetRecognitionRequest(
    val noteId: UUID,
    val fileId: UUID,
    val fileUrl: String
)
