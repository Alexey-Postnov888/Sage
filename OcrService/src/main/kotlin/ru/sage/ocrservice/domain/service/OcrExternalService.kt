package ru.sage.ocrservice.domain.service

interface OcrExternalService {
    fun extractText(fileBytes: ByteArray): String?
}
