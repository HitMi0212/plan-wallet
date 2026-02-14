package com.planwallet.global.error

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * 표준 API 에러 응답.
 */
@Schema(description = "에러 응답")
data class ApiErrorResponse(
    @field:Schema(description = "에러 코드", example = "error.validation")
    val code: String,
    @field:Schema(description = "에러 메시지", example = "Invalid request")
    val message: String,
    @field:Schema(description = "HTTP 상태 코드", example = "400")
    val status: Int,
    @field:Schema(description = "요청 경로", example = "/plan/users")
    val path: String,
    @field:Schema(description = "발생 시각")
    val timestamp: Instant,
)
