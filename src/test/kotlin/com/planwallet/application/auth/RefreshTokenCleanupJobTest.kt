package com.planwallet.application.auth

import com.planwallet.infrastructure.auth.RefreshTokenRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

/**
 * RefreshTokenCleanupJob 단위 테스트.
 */
class RefreshTokenCleanupJobTest {
    private val refreshTokenRepository = mockk<RefreshTokenRepository>()

    private val job = RefreshTokenCleanupJob(refreshTokenRepository)

    @Test
    fun `cleanupExpiredTokens deletes expired tokens`() {
        every { refreshTokenRepository.deleteAllByExpiresAtBefore(any()) } returns 1

        job.cleanupExpiredTokens()

        verify(exactly = 1) { refreshTokenRepository.deleteAllByExpiresAtBefore(any()) }
    }
}
