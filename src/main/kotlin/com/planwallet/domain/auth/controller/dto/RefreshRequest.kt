package com.planwallet.domain.auth.controller.dto

import jakarta.validation.constraints.NotBlank


data class RefreshRequest(
    @field:NotBlank
    val refreshToken: String,
)
