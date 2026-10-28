package ru.sage.authservice.data.repository

import ru.sage.authservice.data.mapper.toDomain
import ru.sage.authservice.data.service.KeycloakService
import ru.sage.authservice.domain.model.response.TokenResponse
import ru.sage.authservice.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val service: KeycloakService
): AuthRepository {
    override suspend fun login(
        username: String,
        password: String
    ): TokenResponse =
        service.login(username, password).toDomain()

    override suspend fun refreshToken(refreshToken: String): TokenResponse =
        service.refreshToken(refreshToken).toDomain()

    override suspend fun logout(refreshToken: String) {
        service.logout(refreshToken)
    }
}