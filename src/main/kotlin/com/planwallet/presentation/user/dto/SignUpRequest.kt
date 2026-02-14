package com.planwallet.presentation.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * 회원 가입 요청 DTO.
 */
@Schema(description = "회원 가입 요청")
data class SignUpRequest(
    @field:Email
    @field:NotBlank
    @field:Schema(description = "이메일", example = "user@example.com")
    val email: String,
    @field:NotBlank
    @field:Size(min = 8, max = 128)
    @field:Schema(description = "비밀번호", example = "P@ssw0rd123")
    val password: String,
    @field:NotBlank
    @field:Size(min = 2, max = 30)
    @field:Schema(description = "닉네임", example = "planuser")
    val nickname: String,
)
