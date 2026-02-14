package com.planwallet.domain.auth

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
)
