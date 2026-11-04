package ru.sage.ocrservice.data.service.model.serviceDTO

data class YandexOcrRequest(
    val folderId: String,
    val analyzeSpecs: List<AnalyzeSpec>
)

data class AnalyzeSpec(
    val content: String, // Base64 файла
    val features: List<Feature>
)

data class Feature(
    val type: String = "TEXT_DETECTION",
    val textDetectionConfig: TextDetectionConfig = TextDetectionConfig()
)

data class TextDetectionConfig(
    val languageCodes: List<String> = listOf("ru", "en")
)