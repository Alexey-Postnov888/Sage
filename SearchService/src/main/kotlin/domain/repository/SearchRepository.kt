package ru.sage.searchservice.domain.repository

import ru.sage.searchservice.domain.model.SearchResult

interface SearchRepository {
    suspend fun search(query: String, userId: String, limit: Int = 10): List<SearchResult>
}