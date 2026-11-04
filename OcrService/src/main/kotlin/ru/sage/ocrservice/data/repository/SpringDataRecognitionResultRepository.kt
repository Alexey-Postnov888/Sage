package ru.sage.ocrservice.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sage.ocrservice.data.model.entity.RecognitionResultEntity
import java.util.UUID

interface SpringDataRecognitionResultRepository : JpaRepository<RecognitionResultEntity, UUID>{
    fun findByNoteId(noteId: UUID): List<RecognitionResultEntity>?
    fun findByFileId(fileId: UUID): RecognitionResultEntity?
}