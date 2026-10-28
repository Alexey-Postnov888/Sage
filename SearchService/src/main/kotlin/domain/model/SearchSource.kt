package ru.sage.searchservice.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SearchSource {
    ELASTICSEARCH,
    QDRANT,
    HYBRID
}