package ru.sage.ocrservice.presentation.mappers

import ru.sage.ocrservice.domain.model.RecognitionResult
import ru.sage.ocrservice.presentation.model.RecognitionResponsePresentation

fun RecognitionResult.toPresentation(): RecognitionResponsePresentation = RecognitionResponsePresentation(
    id = this.id,
    noteId = this.noteId,
    fileId = this.fileId,
    text = this.text
)