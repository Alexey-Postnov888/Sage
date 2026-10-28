package ru.sage.authservice.domain.usecase

import ru.sage.authservice.data.model.exception.CustomExceptions
import ru.sage.authservice.domain.model.response.TokenResponse
import ru.sage.authservice.domain.repository.AuthRepository

interface RefreshTokenUseCase {
    suspend operator fun invoke(refreshToken: String): TokenResponse
}
class RefreshTokenUseCaseImpl(
    private val authRepository: AuthRepository
) : RefreshTokenUseCase {
    override suspend operator fun invoke(refreshToken: String): TokenResponse {
        if (refreshToken.isBlank()) throw CustomExceptions.RefreshTokenException("Refresh token is required")

        return authRepository.refreshToken(refreshToken)
    }
}