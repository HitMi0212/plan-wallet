package com.planwallet.application.auth

import com.planwallet.domain.auth.TokenPair

/**
 * 인증 유스케이스 정의.
 */
interface AuthService {
    /**
     * 이메일/비밀번호로 로그인한다.
     */
    fun login(email: String, password: String): TokenPair

    /**
     * 리프레시 토큰으로 재발급한다.
     */
    fun refresh(refreshToken: String): TokenPair
}
