package com.planwallet.infrastructure.auth

import com.planwallet.domain.auth.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

/**
 * 리프레시 토큰 JPA 리포지토리.
 */
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    /**
     * 토큰 값으로 조회.
     */
    fun findByToken(token: String): RefreshToken?

    /**
     * 사용자 ID 기준 삭제.
     */
    fun deleteAllByUserId(userId: Long)

    /**
     * 만료 토큰 정리.
     */
    fun deleteAllByExpiresAtBefore(expiresAt: Instant): Long
}
