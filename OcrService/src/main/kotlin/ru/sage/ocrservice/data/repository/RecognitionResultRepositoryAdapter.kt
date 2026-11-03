package ru.sage.ocrservice.data.repository

import org.springframework.stereotype.Repository
import ru.sage.ocrservice.data.model.entity.RecognitionResultEntity
import ru.sage.ocrservice.domain.model.RecognitionResult
import ru.sage.ocrservice.domain.repository.RecognitionResultRepository
import java.util.UUID

@Repository
class RecognitionResultRepositoryAdapter (
    private val repository: SpringDataRecognitionResultRepository
) : RecognitionResultRepository {
    override fun save(recognitionResult: RecognitionResult): RecognitionResult {
        val entity = toEntity(recognitionResult)
        val result = repository.save(entity)
        return toDomain(result)
    }

    override fun findById(id: UUID): RecognitionResult? {
        val entity = repository.findById(id).orElse(null)
        if (entity != null) {
            return toDomain(entity)
        }
        return null
    }

    override fun findByNoteId(noteId: UUID): List<RecognitionResult>? {
        val entity = repository.findByNoteId(noteId)
        if (entity != null) {
            return entity.map{toDomain(it)}
        }
        return null
    }

    override fun findByFileId(fileId: UUID): RecognitionResult? {
        val entity = repository.findByFileId(fileId)
        if (entity != null) {
            return toDomain(entity)
        }
        return null
    }

    private fun toEntity(domain: RecognitionResult) : RecognitionResultEntity {
        return RecognitionResultEntity(
            id = domain.id,
            noteId = domain.noteId,
            fileId = domain.fileId,
            fileUrl = domain.fileUrl,
            text = domain.text
        )
    }

    private fun toDomain(entity: RecognitionResultEntity) : RecognitionResult {
        return RecognitionResult(
            id = entity.id,
            noteId = entity.noteId,
            fileId = entity.fileId,
            fileUrl = entity.fileUrl,
            text = entity.text
        )
    }
}