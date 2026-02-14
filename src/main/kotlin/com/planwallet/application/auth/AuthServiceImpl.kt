package com.planwallet.application.auth

import com.planwallet.domain.auth.RefreshToken
import com.planwallet.domain.auth.TokenPair
import com.planwallet.global.security.JwtProperties
import com.planwallet.global.security.JwtTokenProvider
import com.planwallet.infrastructure.auth.RefreshTokenRepository
import com.planwallet.infrastructure.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * 인증 관련 유스케이스를 수행하는 애플리케이션 서비스.
 */
@Service
class AuthServiceImpl(
    private val tokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProperties: JwtProperties,
) : AuthService {
    /**
     * 이메일/비밀번호로 로그인하고 토큰을 발급한다.
     */
    override fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        if (!passwordEncoder.matches(password, user.passwordHash)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        val subject = user.id?.toString()
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User id not available")
        val accessToken = tokenProvider.createAccessToken(subject)
        val refreshToken = tokenProvider.createRefreshToken(subject)

        rotateRefreshToken(user.id!!, refreshToken)

        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    /**
     * 유효한 리프레시 토큰으로 새 토큰을 발급한다.
     */
    override fun refresh(refreshToken: String): TokenPair {
        if (!tokenProvider.isValid(refreshToken) || !tokenProvider.isRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }

        val subject = tokenProvider.getSubject(refreshToken)
        val userId = subject.toLongOrNull()
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")

        val stored = refreshTokenRepository.findByToken(refreshToken)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        if (stored.userId != userId) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }

        val newAccess = tokenProvider.createAccessToken(subject)
        val newRefresh = tokenProvider.createRefreshToken(subject)

        rotateRefreshToken(userId, newRefresh)

        return TokenPair(
            accessToken = newAccess,
            refreshToken = newRefresh,
        )
    }

    private fun rotateRefreshToken(userId: Long, token: String) {
        refreshTokenRepository.deleteAllByUserId(userId)
        val expiresAt = Instant.now().plus(jwtProperties.refreshTokenDays, ChronoUnit.DAYS)
        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                token = token,
                expiresAt = expiresAt,
            )
        )
    }
}
