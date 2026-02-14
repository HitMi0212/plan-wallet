package com.planwallet.application.auth

import com.planwallet.infrastructure.auth.RefreshTokenRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * 만료 리프레시 토큰 정리 작업.
 */
@Component
class RefreshTokenCleanupJob(
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    /**
     * 매일 자정 만료 토큰 정리.
     */
    @Scheduled(cron = "0 0 0 * * *")
    fun cleanupExpiredTokens() {
        refreshTokenRepository.deleteAllByExpiresAtBefore(Instant.now())
    }
}
