package ru.sage.searchservice.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmbedRequestDto(
    @SerialName("texts")
    val texts: List<String>,
)
