package com.planwallet.global.error

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 표준 API 에러 응답.
 */
@Schema(description = "에러 응답")
data class ApiErrorResponse(
    @field:Schema(description = "에러 코드", example = "error.validation")
    val code: String,
    @field:Schema(description = "에러 메시지", example = "Invalid request")
    val message: String,
)
