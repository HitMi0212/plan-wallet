package com.planwallet.application.auth

import com.planwallet.domain.auth.TokenPair
import com.planwallet.global.security.JwtTokenProvider
import com.planwallet.infrastructure.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

/**
 * 인증 관련 유스케이스를 수행하는 애플리케이션 서비스.
 */
@Service
class AuthServiceImpl(
    private val tokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
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
        return TokenPair(
            accessToken = tokenProvider.createAccessToken(subject),
            refreshToken = tokenProvider.createRefreshToken(subject),
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
        return TokenPair(
            accessToken = tokenProvider.createAccessToken(subject),
            refreshToken = tokenProvider.createRefreshToken(subject),
        )
    }
}
