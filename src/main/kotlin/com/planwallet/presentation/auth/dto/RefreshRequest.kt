package com.planwallet.presentation.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 토큰 재발급 요청 DTO.
 */
@Schema(description = "토큰 재발급 요청")
data class RefreshRequest(
    @field:NotBlank
    @field:Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refreshToken: String,
)
