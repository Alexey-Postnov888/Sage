package ru.sage.indexservice.data.repository

import ru.sage.indexservice.data.service.ElasticsearchService
import ru.sage.indexservice.data.service.EmbeddingService
import ru.sage.indexservice.data.utils.Chunker
import ru.sage.indexservice.domain.repository.IndexRepository

class IndexRepositoryImpl(
    private val elasticsearchService: ElasticsearchService,
    private val embeddingService: EmbeddingService,
    private val chunker: Chunker
): IndexRepository {
    override suspend fun index(noteId: String, text: String, userId: String) {
        try {
            val chunks = chunker.chunk(text)
            embeddingService.embed(
                noteId = noteId,
                chunks = chunks,
                userId = userId,
            )
            elasticsearchService.index(noteId, text, userId)
        } catch (e: Exception) {
            println(e)
        }
    }
}