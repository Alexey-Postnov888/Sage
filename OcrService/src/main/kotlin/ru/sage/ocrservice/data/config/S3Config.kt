package ru.sage.ocrservice.data.config

data class S3Config(
    val endpoint: String,
    val accessKey: String,
    val secretKey: String
) {
    companion object {
        fun fromEnv() = S3Config(
            endpoint = requireNotNull(System.getenv("S3_ENDPOINT")),
            accessKey = requireNotNull(System.getenv("S3_ACCESS_KEY")),
            secretKey = requireNotNull(System.getenv("S3_SECRET_KEY"))
        )
    }
}