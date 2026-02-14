package com.planwallet.application.user

import com.planwallet.domain.user.User

/**
 * 사용자 유스케이스 정의.
 */
interface UserService {
    /**
     * 회원 가입 처리.
     */
    fun register(email: String, password: String, nickname: String): User

    /**
     * 사용자 ID로 조회.
     */
    fun getById(id: Long): User
}
