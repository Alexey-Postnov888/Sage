package ru.sage.authservice.data.di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.sage.authservice.data.config.KeycloakConfig
import ru.sage.authservice.data.repository.AuthRepositoryImpl
import ru.sage.authservice.data.service.KeycloakService
import ru.sage.authservice.data.service.KeycloakServiceImpl
import ru.sage.authservice.domain.repository.AuthRepository
import ru.sage.authservice.domain.usecase.LoginUseCase
import ru.sage.authservice.domain.usecase.LoginUseCaseImpl
import ru.sage.authservice.domain.usecase.RefreshTokenUseCase
import ru.sage.authservice.domain.usecase.RefreshTokenUseCaseImpl
import ru.sage.authservice.presentation.router.grpc.AuthGrpcService

val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
}

val configModule = module {
    single { KeycloakConfig.fromEnv() }
}

val serviceModule = module {
    singleOf(::KeycloakServiceImpl) bind KeycloakService::class
}

val repositoryModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
}

val useCaseModule = module {
    singleOf(::LoginUseCaseImpl) bind LoginUseCase::class
    singleOf(::RefreshTokenUseCaseImpl) bind RefreshTokenUseCase::class
}

val grpcModule = module {
    singleOf(::AuthGrpcService)
}

val appModule = module {
    includes(
        networkModule,
        configModule,
        serviceModule,
        repositoryModule,
        useCaseModule,
        grpcModule
    )
}