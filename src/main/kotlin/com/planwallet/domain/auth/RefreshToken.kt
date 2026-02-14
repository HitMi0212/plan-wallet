package com.planwallet.domain.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import java.time.Instant

/**
 * 리프레시 토큰 엔티티.
 */
@Schema(description = "리프레시 토큰 엔티티")
@Entity
@Table(name = "refresh_tokens")
class RefreshToken(
    /** 사용자 ID */
    @field:Schema(description = "사용자 ID", example = "1")
    @Column(nullable = false)
    var userId: Long,

    /** 리프레시 토큰 값 */
    @field:Schema(description = "리프레시 토큰")
    @Column(nullable = false, unique = true, length = 512)
    var token: String,

    /** 만료 시각 */
    @field:Schema(description = "만료 시각")
    @Column(nullable = false)
    var expiresAt: Instant,
) {
    /** 식별자 */
    @field:Schema(description = "ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /** 생성 시각 */
    @field:Schema(description = "생성 시각")
    @Column(nullable = false)
    var createdAt: Instant? = null
        private set

    @PrePersist
    fun onCreate() {
        createdAt = Instant.now()
    }
}
