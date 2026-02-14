package com.planwallet.presentation.auth

import com.planwallet.application.auth.AuthService
import com.planwallet.presentation.auth.dto.LoginRequest
import com.planwallet.presentation.auth.dto.RefreshRequest
import com.planwallet.presentation.auth.dto.TokenResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/plan/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): TokenResponse {
        val tokens = authService.login(request.email, request.password)
        return TokenResponse(tokens.accessToken, tokens.refreshToken)
    }

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshRequest): TokenResponse {
        val tokens = authService.refresh(request.refreshToken)
        return TokenResponse(tokens.accessToken, tokens.refreshToken)
    }
}
