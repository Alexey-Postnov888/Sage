package ru.sage.searchservice.presentation.mapper

import ru.sage.searchservice.domain.model.SearchResult
import ru.sage.searchservice.presentation.model.SearchResultPresentation

fun SearchResult.toPresentation() = SearchResultPresentation(
    noteId = noteId,
    text = text,
    score = score,
    source = source
)