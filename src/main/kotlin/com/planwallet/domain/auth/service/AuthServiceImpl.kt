package com.planwallet.domain.auth.service

import com.planwallet.global.security.JwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthServiceImpl(
    private val tokenProvider: JwtTokenProvider,
) : AuthService {
    override fun login(email: String, password: String): TokenPair {
        throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Login not implemented yet")
    }

    override fun refresh(refreshToken: String): TokenPair {
        if (!tokenProvider.isValid(refreshToken) || !tokenProvider.isRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }

        val subject = tokenProvider.getSubject(refreshToken)
        return TokenPair(
            accessToken = tokenProvider.createAccessToken(subject),
            refreshToken = tokenProvider.createRefreshToken(subject),
        )
    }
}
