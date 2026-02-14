package com.planwallet.domain.auth

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 인증 토큰 쌍 모델.
 */
@Schema(description = "액세스/리프레시 토큰 쌍")
data class TokenPair(
    @field:Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,
    @field:Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refreshToken: String,
)
