package ru.sage.ocrservice.data.service

import org.springframework.stereotype.Component
import ru.sage.ocrservice.data.config.YandexConfig
import ru.sage.ocrservice.domain.service.OcrExternalService
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import ru.sage.ocrservice.data.service.model.serviceDTO.AnalyzeSpec
import ru.sage.ocrservice.data.service.model.serviceDTO.Feature
import ru.sage.ocrservice.data.service.model.serviceDTO.YandexOcrRequest
import ru.sage.ocrservice.data.service.model.serviceDTO.YandexOcrResponse
import java.util.Base64

@Component
class OcrExternalServiceImpl : OcrExternalService {

    private val config = YandexConfig.fromEnv()

    private val restClientBuilder = RestClient.builder()
    private val restClient = restClientBuilder
        .baseUrl("https://vision.api.cloud.yandex.net/vision/v1/batchAnalyze")
        .defaultHeader("Authorization", "Api-Key ${config.apiKey}")
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build()

    override fun extractText(fileBytes: ByteArray): String? {
        val base64Content = Base64.getEncoder().encodeToString(fileBytes)

        val requestBody = YandexOcrRequest(
            folderId = config.folderId,
            analyzeSpecs = listOf(
                AnalyzeSpec(
                    content = base64Content,
                    features = listOf(Feature())
                )
            )
        )

        return restClient.post()
            .body(requestBody)
            .retrieve()
            .body<YandexOcrResponse>()
            ?.let { parseExtractedText(it) } ?: ""
//        return "Это тестовый текст"
    }

    private fun parseExtractedText(response: YandexOcrResponse?): String {
        val textBuilder = StringBuilder()

        response?.results?.forEach { batchResult ->
            batchResult?.results?.forEach { detectionResult ->
                detectionResult?.textDetection?.pages?.forEach { page ->
                    page?.blocks?.forEach { block ->
                        block?.lines?.forEach { line ->
                            val lineText = line?.words?.mapNotNull { it?.text }?.joinToString(" ")
                            if (!lineText.isNullOrBlank()) {
                                textBuilder.append(lineText).append("\n")
                            }
                        }
                    }
                }
            }
        }

        return textBuilder.toString().trim()
    }
}