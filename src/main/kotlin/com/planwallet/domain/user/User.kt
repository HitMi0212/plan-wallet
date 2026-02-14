package com.planwallet.domain.user

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
@Entity
@Table(name = "users")
class User(
    @Column(nullable = false, unique = true)
    var email: String,
    @Column(nullable = false)
    var passwordHash: String,
    @Column(nullable = false)
    var nickname: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var createdAt: Instant? = null
        private set

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
