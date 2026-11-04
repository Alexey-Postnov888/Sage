package ru.sage.ocrservice.domain.service

interface FileDownloadService {
    fun downloadFile(bucketName: String, objectKey: String): ByteArray
}