package ru.sage.searchservice.presentation.route.grpc

import ru.sage.searchservice.domain.model.SearchSource
import ru.sage.searchservice.domain.usecase.SearchUseCase
import ru.sage.searchservice.grpc.SearchRequest
import ru.sage.searchservice.grpc.SearchResponse
import ru.sage.searchservice.grpc.SearchResult
import ru.sage.searchservice.grpc.SearchServiceGrpcKt
import ru.sage.searchservice.grpc.SearchSource as GrpcSearchSource
import ru.sage.searchservice.presentation.route.grpc.utils.getCurrentUserId
import ru.sage.searchservice.presentation.route.grpc.utils.grpcCall

class SearchGrpcService(
    private val searchUseCase: SearchUseCase
) : SearchServiceGrpcKt.SearchServiceCoroutineImplBase() {
    override suspend fun search(request: SearchRequest): SearchResponse = grpcCall {
        val userId = getCurrentUserId()
        val limit = if (request.hasLimit()) request.limit else 10

        val results = searchUseCase(
            query = request.query,
            userId = userId,
            limit = limit
        )

        SearchResponse.newBuilder()
            .addAllResults(
                results.map { result ->
                    SearchResult.newBuilder()
                        .setNoteId(result.noteId)
                        .setText(result.text)
                        .setScore(result.score)
                        .setSource(result.source.toGrpc())
                        .build()
                }
            ).build()
    }

    private fun SearchSource.toGrpc(): GrpcSearchSource =
        when (this) {
            SearchSource.ELASTICSEARCH -> GrpcSearchSource.ELASTICSEARCH
            SearchSource.QDRANT -> GrpcSearchSource.QDRANT
            SearchSource.HYBRID -> GrpcSearchSource.HYBRID
        }
}