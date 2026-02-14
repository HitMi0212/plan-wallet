package com.planwallet.infrastructure.user

import com.planwallet.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * 사용자 JPA 리포지토리.
 */
interface UserRepository : JpaRepository<User, Long> {
    /**
     * 이메일로 사용자 조회.
     */
    fun findByEmail(email: String): User?

    /**
     * 이메일 존재 여부 확인.
     */
    fun existsByEmail(email: String): Boolean
}
