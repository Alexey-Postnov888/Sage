package ru.sage.authservice.domain.usecase

import ru.sage.authservice.data.model.exception.CustomExceptions
import ru.sage.authservice.domain.model.response.TokenResponse
import ru.sage.authservice.domain.repository.AuthRepository

interface LoginUseCase {
    suspend operator fun invoke(username: String, password: String): TokenResponse
}

class LoginUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginUseCase {
    override suspend operator fun invoke(username: String, password: String): TokenResponse {
        if (username.isBlank())
            throw CustomExceptions.LoginException.LoginUsernameException("Username is required")

        if (password.isBlank())
            throw CustomExceptions.LoginException.LoginPasswordException("Password is required")

        return authRepository.login(username, password)
    }
}