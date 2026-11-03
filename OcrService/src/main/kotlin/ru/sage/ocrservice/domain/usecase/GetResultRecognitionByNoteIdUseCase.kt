package ru.sage.ocrservice.domain.usecase

import org.springframework.stereotype.Service
import ru.sage.ocrservice.domain.model.RecognitionResult
import ru.sage.ocrservice.domain.repository.RecognitionResultRepository
import java.util.UUID

interface GetResultRecognitionByNoteIdUseCase {
    fun execute(id: UUID) : List<RecognitionResult>?
}

@Service
class GetResultRecognitionByNoteIdUseCaseImpl (
    private val repository: RecognitionResultRepository
) : GetResultRecognitionByNoteIdUseCase{
    override fun execute(id: UUID): List<RecognitionResult>? {
        return repository.findByNoteId(id)
    }
}