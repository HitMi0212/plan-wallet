package com.planwallet.domain.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.Instant

/**
 * 사용자 엔티티.
 */
@Schema(description = "사용자 엔티티")
@Entity
@Table(name = "users")
class User(
    /** 사용자 이메일 */
    @field:Schema(description = "이메일", example = "user@example.com")
    @Column(nullable = false, unique = true)
    var email: String,

    /** 비밀번호 해시 */
    @field:Schema(description = "비밀번호 해시")
    @Column(nullable = false)
    var passwordHash: String,

    /** 닉네임 */
    @field:Schema(description = "닉네임", example = "planuser")
    @Column(nullable = false)
    var nickname: String,
) {
    /** 사용자 ID */
    @field:Schema(description = "사용자 ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /** 생성 시각 */
    @field:Schema(description = "생성 시각")
    @Column(nullable = false)
    var createdAt: Instant? = null
        private set

    /** 수정 시각 */
    @field:Schema(description = "수정 시각")
    @Column(nullable = false)
    var updatedAt: Instant? = null
        private set

    /**
     * 비밀번호 해시를 변경한다.
     */
    fun changePassword(newPasswordHash: String) {
        passwordHash = newPasswordHash
    }

    /**
     * 닉네임을 변경한다.
     */
    fun changeNickname(newNickname: String) {
        nickname = newNickname
    }

    @PrePersist
    fun onCreate() {
        val now = Instant.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}
