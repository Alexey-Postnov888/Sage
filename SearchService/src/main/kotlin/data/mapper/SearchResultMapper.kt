package ru.sage.searchservice.data.mapper

import ru.sage.searchservice.data.model.SearchResultDto
import ru.sage.searchservice.domain.model.SearchResult

fun SearchResultDto.toDomain(): SearchResult = SearchResult(
    noteId = noteId,
    text = text,
    score = score,
    source = source
)