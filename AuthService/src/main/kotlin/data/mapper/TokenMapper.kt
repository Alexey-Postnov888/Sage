package ru.sage.authservice.data.mapper

import ru.sage.authservice.data.model.response.TokenResponseDto
import ru.sage.authservice.domain.model.response.TokenResponse

fun TokenResponseDto.toDomain(): TokenResponse = TokenResponse(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    refreshExpiresIn = refreshExpiresIn
)