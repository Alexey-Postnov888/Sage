package ru.sage.searchservice.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmbeddingsResponseDto(
    @SerialName("embeddings")
    val embeddings: List<List<Float>>
)