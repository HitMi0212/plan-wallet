package com.planwallet.presentation.auth.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
