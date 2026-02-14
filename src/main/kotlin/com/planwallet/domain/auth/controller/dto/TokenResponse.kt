package com.planwallet.domain.auth.controller.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
