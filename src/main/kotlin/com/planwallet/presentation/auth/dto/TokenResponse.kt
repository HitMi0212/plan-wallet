package com.planwallet.presentation.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 토큰 응답 DTO.
 */
@Schema(description = "토큰 응답")
data class TokenResponse(
    @field:Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,
    @field:Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refreshToken: String,
)
