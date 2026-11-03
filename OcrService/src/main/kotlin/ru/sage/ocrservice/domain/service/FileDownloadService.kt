package ru.sage.ocrservice.domain.service

interface FileDownloadService {
    fun downloadFile(fileUrl: String): ByteArray
}