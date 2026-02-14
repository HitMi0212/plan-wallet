package com.planwallet.application.auth

import com.planwallet.domain.auth.RefreshToken
import com.planwallet.domain.auth.TokenPair
import com.planwallet.domain.user.User
import com.planwallet.global.security.JwtProperties
import com.planwallet.global.security.JwtTokenProvider
import com.planwallet.infrastructure.auth.RefreshTokenRepository
import com.planwallet.infrastructure.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.server.ResponseStatusException

/**
 * AuthServiceImpl 단위 테스트.
 */
class AuthServiceImplTest {
    private val tokenProvider = mockk<JwtTokenProvider>()
    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val refreshTokenRepository = mockk<RefreshTokenRepository>()
    private val jwtProperties = JwtProperties(
        issuer = "plan-wallet",
        secret = "plan-wallet-dev-secret-please-change-32bytes",
        accessTokenMinutes = 30,
        refreshTokenDays = 14,
    )

    private val authService = AuthServiceImpl(
        tokenProvider = tokenProvider,
        userRepository = userRepository,
        passwordEncoder = passwordEncoder,
        refreshTokenRepository = refreshTokenRepository,
        jwtProperties = jwtProperties,
    )

    @Test
    fun `login issues tokens when credentials are valid`() {
        val user = User(
            email = "user@example.com",
            passwordHash = "hash",
            nickname = "tester",
        ).apply { id = 1L }

        every { userRepository.findByEmail("user@example.com") } returns user
        every { passwordEncoder.matches("P@ssw0rd123", "hash") } returns true
        every { tokenProvider.createAccessToken("1") } returns "access"
        every { tokenProvider.createRefreshToken("1") } returns "refresh"
        every { refreshTokenRepository.deleteAllByUserId(1L) } returns Unit
        every { refreshTokenRepository.save(any()) } answers { firstArg() }

        val result = authService.login("user@example.com", "P@ssw0rd123")

        assertEquals(TokenPair("access", "refresh"), result)
        verify(exactly = 1) { userRepository.findByEmail("user@example.com") }
    }

    @Test
    fun `login throws unauthorized when user not found`() {
        every { userRepository.findByEmail("missing@example.com") } returns null

        val exception = assertThrows(ResponseStatusException::class.java) {
            authService.login("missing@example.com", "P@ssw0rd123")
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.statusCode)
    }

    @Test
    fun `login throws unauthorized when password mismatch`() {
        val user = User(
            email = "user@example.com",
            passwordHash = "hash",
            nickname = "tester",
        ).apply { id = 1L }

        every { userRepository.findByEmail("user@example.com") } returns user
        every { passwordEncoder.matches("wrong", "hash") } returns false

        val exception = assertThrows(ResponseStatusException::class.java) {
            authService.login("user@example.com", "wrong")
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.statusCode)
    }

    @Test
    fun `refresh throws unauthorized when token invalid`() {
        every { tokenProvider.isValid("bad") } returns false

        val exception = assertThrows(ResponseStatusException::class.java) {
            authService.refresh("bad")
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.statusCode)
    }

    @Test
    fun `refresh issues new tokens when refresh token valid`() {
        every { tokenProvider.isValid("refresh") } returns true
        every { tokenProvider.isRefreshToken("refresh") } returns true
        every { tokenProvider.getSubject("refresh") } returns "1"
        every { refreshTokenRepository.findByToken("refresh") } returns RefreshToken(1L, "refresh", java.time.Instant.now())
        every { tokenProvider.createAccessToken("1") } returns "access"
        every { tokenProvider.createRefreshToken("1") } returns "refresh2"
        every { refreshTokenRepository.deleteAllByUserId(1L) } returns Unit
        every { refreshTokenRepository.save(any()) } answers { firstArg() }

        val result = authService.refresh("refresh")

        assertEquals(TokenPair("access", "refresh2"), result)
    }
}
