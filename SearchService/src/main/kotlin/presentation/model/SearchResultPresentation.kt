package ru.sage.searchservice.presentation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sage.searchservice.domain.model.SearchSource

@Serializable
data class SearchResultPresentation(
    @SerialName("note_id") val noteId: String,
    @SerialName("text") val text: String,
    @SerialName("score") val score: Double,
    @SerialName("source") val source: SearchSource
)
