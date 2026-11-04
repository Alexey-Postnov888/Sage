package ru.sage.ocrservice.domain.usecase

import org.springframework.stereotype.Service
import ru.sage.ocrservice.domain.model.RecognitionResult
import ru.sage.ocrservice.domain.repository.RecognitionResultRepository
import java.util.UUID

interface GetResultRecognitionByFileIdUseCase {
    fun execute(id: UUID) : RecognitionResult?
}

@Service
class GetResultRecognitionByFileIdUseCaseImpl (
    private val repository: RecognitionResultRepository
): GetResultRecognitionByFileIdUseCase {
    override fun execute(id: UUID): RecognitionResult? {
        return repository.findByFileId(id)
    }
}