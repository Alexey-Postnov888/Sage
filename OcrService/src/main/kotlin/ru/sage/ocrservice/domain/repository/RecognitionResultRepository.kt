package ru.sage.ocrservice.domain.repository

import ru.sage.ocrservice.domain.model.RecognitionResult
import java.util.UUID

interface RecognitionResultRepository {
    fun save(recognitionResult: RecognitionResult): RecognitionResult
    fun findById(id: UUID): RecognitionResult?
    fun findByNoteId(noteId: UUID): List<RecognitionResult>?
    fun findByFileId(fileId: UUID): RecognitionResult?
}