package com.planwallet.presentation.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * 사용자 응답 DTO.
 */
@Schema(description = "사용자 정보 응답")
data class UserResponse(
    @field:Schema(description = "사용자 ID", example = "1")
    val id: Long,
    @field:Schema(description = "이메일", example = "user@example.com")
    val email: String,
    @field:Schema(description = "닉네임", example = "planuser")
    val nickname: String,
    @field:Schema(description = "가입 일시")
    val createdAt: Instant,
)
