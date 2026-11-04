package ru.sage.ocrservice.data.service

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import ru.sage.ocrservice.data.config.S3Config
import ru.sage.ocrservice.domain.service.FileDownloadService
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.net.URI

@Component
class FileDownloadServiceImpl : FileDownloadService {
    override fun downloadFile(bucketName: String, objectKey: String): ByteArray {
        return try {
            val getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build()

            s3Client().getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray()
        } catch (e: Exception) {
            throw RuntimeException("Не удалось скачать файл из бакета '$bucketName' по ключу '$objectKey': ${e.message}", e)
        }
    }

    private fun s3Client(): S3Client {
        val config = S3Config.fromEnv()
        val credentials = AwsBasicCredentials.create(config.accessKey, config.secretKey)

        return S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(config.endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(true) // Обязательно для SeaweedFS/MinIO
                    .build()
            )
            .build()
    }

    private fun getTestImage(): ByteArray {
        val resourceStream = this::class.java.classLoader.getResourceAsStream("test-image.png")
            ?: throw RuntimeException("test-image not found")

        return resourceStream.readAllBytes()
    }
}