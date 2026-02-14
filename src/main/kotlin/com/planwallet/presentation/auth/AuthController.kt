package com.planwallet.presentation.auth

import com.planwallet.application.auth.AuthService
import com.planwallet.presentation.auth.dto.LoginRequest
import com.planwallet.presentation.auth.dto.RefreshRequest
import com.planwallet.presentation.auth.dto.TokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 인증 관련 API 컨트롤러.
 */
@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/plan/auth")
class AuthController(
    private val authService: AuthService,
) {
    /**
     * 로그인하여 토큰을 발급한다.
     */
    @Operation(summary = "로그인", description = "이메일/비밀번호로 로그인하고 토큰을 발급합니다.")
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): TokenResponse {
        val tokens = authService.login(request.email, request.password)
        return TokenResponse(tokens.accessToken, tokens.refreshToken)
    }

    /**
     * 리프레시 토큰으로 재발급한다.
     */
    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 액세스/리프레시 토큰을 재발급합니다.")
    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshRequest): TokenResponse {
        val tokens = authService.refresh(request.refreshToken)
        return TokenResponse(tokens.accessToken, tokens.refreshToken)
    }
}
