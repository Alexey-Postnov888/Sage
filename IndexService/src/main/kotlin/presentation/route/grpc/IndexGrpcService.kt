package ru.sage.indexservice.presentation.route.grpc

import ru.sage.indexservice.domain.usecase.IndexUseCase
import ru.sage.indexservice.grpc.IndexRequest
import ru.sage.indexservice.grpc.IndexResponse
import ru.sage.indexservice.grpc.IndexServiceGrpcKt
import ru.sage.indexservice.presentation.route.grpc.utils.getCurrentUserId
import ru.sage.indexservice.presentation.route.grpc.utils.grpcCall

class IndexGrpcService(
    private val indexUseCase: IndexUseCase
) : IndexServiceGrpcKt.IndexServiceCoroutineImplBase() {
    override suspend fun index(request: IndexRequest): IndexResponse = grpcCall {
        val userId = getCurrentUserId()

        indexUseCase(
            noteId = request.noteId,
            text = request.text,
            userId = userId
        )

        IndexResponse.newBuilder()
            .setSuccess(true)
            .setMessage("Text indexed successfully")
            .build()
    }
}