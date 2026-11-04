package ru.sage.ocrservice.data.config

data class YandexConfig (
    val apiKey: String,
    val folderId: String,
    val ocrModel: String
){
    companion object{
        fun fromEnv() = YandexConfig(
            apiKey = requireNotNull(System.getenv("YANDEX_CLOUD_API_KEY")),
            folderId = requireNotNull(System.getenv("YANDEX_CLOUD_FOLDER")),
            ocrModel = requireNotNull(System.getenv("YANDEX_CLOUD_MODEL"))
        )
    }
}