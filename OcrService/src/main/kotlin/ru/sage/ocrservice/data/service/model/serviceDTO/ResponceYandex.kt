package ru.sage.ocrservice.data.service.model.serviceDTO

data class YandexOcrResponse(
    val results: List<OCRResult?>?
)

data class OCRResult(
    val results: List<TextDetectionResult?>?
)

data class TextDetectionResult(
    val textDetection: TextDetection?
)

data class TextDetection(
    val pages: List<Page?>?
)

data class Page(
    val blocks: List<Block?>?
)

data class Block(
    val lines: List<Line?>?
)

data class Line(
    val words: List<Word?>?
)

data class Word(
    val text: String?
)