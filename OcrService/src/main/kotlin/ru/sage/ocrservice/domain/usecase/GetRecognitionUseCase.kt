package ru.sage.ocrservice.domain.usecase

import org.springframework.stereotype.Service
import ru.sage.ocrservice.domain.model.RecognitionResult
import ru.sage.ocrservice.domain.repository.RecognitionResultRepository
import ru.sage.ocrservice.domain.service.FileDownloadService
import ru.sage.ocrservice.domain.service.OcrExternalService
import java.util.UUID

interface GetRecognitionUseCase {
    fun execute(noteId: UUID, fileId: UUID, bucketName: String, objectKey: String,): RecognitionResult?
}

@Service
class GetRecognitionUseCaseImpl(
    private val ocrExternalService: OcrExternalService,
    private val fileDownloadService: FileDownloadService,
    private val repository: RecognitionResultRepository
) : GetRecognitionUseCase {
    override fun execute(
        noteId: UUID,
        fileId: UUID,
        bucketName: String,
        objectKey: String,
    ): RecognitionResult? {

        if(repository.findByFileId(fileId) != null) {
            return repository.findByFileId(fileId)
        }

        val fileByte = fileDownloadService.downloadFile(
            bucketName = bucketName,
            objectKey = objectKey
        )
        val text = ocrExternalService.extractText(fileByte)

        val result = RecognitionResult(
            noteId = noteId,
            fileId = fileId,
            text = text
        )

        return repository.save(result)
    }
}