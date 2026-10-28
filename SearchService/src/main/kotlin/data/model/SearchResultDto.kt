package ru.sage.searchservice.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sage.searchservice.domain.model.SearchSource

@Serializable
data class SearchResultDto(
    @SerialName("note_id") val noteId: String,
    @SerialName("text") val text: String,
    @SerialName("score") val score: Double,
    @SerialName("source") val source: SearchSource
)