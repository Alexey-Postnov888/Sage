package ru.sage.ocrservice.data.service

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import ru.sage.ocrservice.domain.service.FileDownloadService

@Component
class FileDownloadServiceImpl : FileDownloadService {
    private val restClient = RestClient.create()

    override fun downloadFile(fileUrl: String): ByteArray {
        return try {
            restClient.get()
                .uri(fileUrl)
                .retrieve()
                .body<ByteArray>()
                ?: throw RuntimeException("Received empty file from S3 URL: $fileUrl")
        } catch (e: Exception) {
            throw RuntimeException("Failed to download file from S3: ${e.message}", e)
        }
    }

    private fun getTestImage(): ByteArray {
        val resourceStream = this::class.java.classLoader.getResourceAsStream("test-image.png")
            ?: throw RuntimeException("test-image not found")

        return resourceStream.readAllBytes()
    }
}