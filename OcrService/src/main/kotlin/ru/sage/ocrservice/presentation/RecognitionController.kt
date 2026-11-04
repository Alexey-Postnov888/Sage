package ru.sage.ocrservice.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sage.ocrservice.domain.model.request.GetRecognitionRequest
import ru.sage.ocrservice.domain.usecase.GetRecognitionUseCase
import ru.sage.ocrservice.domain.usecase.GetResultRecognitionByFileIdUseCase
import ru.sage.ocrservice.domain.usecase.GetResultRecognitionByNoteIdUseCase
import ru.sage.ocrservice.presentation.mappers.toPresentation
import ru.sage.ocrservice.presentation.model.ErrorResponse
import java.util.UUID

@RestController
@RequestMapping("/api/ocr")
class RecognitionController (
    private val getRecognitionUseCase: GetRecognitionUseCase,
    private val getResultRecognitionByFileIdUseCase: GetResultRecognitionByFileIdUseCase,
    private val getResultRecognitionByNoteIdUseCase: GetResultRecognitionByNoteIdUseCase
) {

    @PostMapping("/")
    fun getRecognition(@RequestBody request: GetRecognitionRequest): Any {
        val result = getRecognitionUseCase.execute(
            noteId = request.noteId,
            fileId = request.fileId,
            bucketName = request.bucketName,
            objectKey = request.objectKey
        )
        if (result != null){
            return result.toPresentation()
        }
        return ErrorResponse(
            status = 500,
            message = "Not saved"
        )
    }

    @GetMapping("/file/{fileId}")
    fun getRecognitionByFileId(@PathVariable fileId: String): Any {
        val result = getResultRecognitionByFileIdUseCase.execute(UUID.fromString(fileId))
        if(result != null){
            return result.toPresentation()
        }

        return ErrorResponse(
            status = 500,
            message = "Not found by file id $fileId"
        )
    }

    @GetMapping("/note/{noteId}")
    fun getRecognitionByNoteId(@PathVariable noteId: String): Any {
        val result = getResultRecognitionByNoteIdUseCase.execute(UUID.fromString(noteId))
        if(result != null){
            return result.map { it.toPresentation() }
        }

        return ErrorResponse(
            status = 500,
            message = "Not found by note id $noteId"
        )
    }
}