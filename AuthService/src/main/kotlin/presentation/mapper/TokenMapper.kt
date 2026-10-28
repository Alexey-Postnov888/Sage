package ru.sage.authservice.presentation.mapper

import ru.sage.authservice.domain.model.response.TokenResponse
import ru.sage.authservice.presentation.model.response.TokenResponsePresentation

fun TokenResponse.toPresentation(): TokenResponsePresentation = TokenResponsePresentation(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    refreshExpiresIn = refreshExpiresIn
)