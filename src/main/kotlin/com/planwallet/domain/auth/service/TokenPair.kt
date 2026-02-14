package com.planwallet.domain.auth.service

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
)
