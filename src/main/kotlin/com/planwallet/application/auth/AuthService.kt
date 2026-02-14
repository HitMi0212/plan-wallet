package com.planwallet.application.auth

import com.planwallet.domain.auth.TokenPair

interface AuthService {
    fun login(email: String, password: String): TokenPair
    fun refresh(refreshToken: String): TokenPair
}
