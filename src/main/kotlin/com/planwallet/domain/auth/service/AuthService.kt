package com.planwallet.domain.auth.service

interface AuthService {
    fun login(email: String, password: String): TokenPair
    fun refresh(refreshToken: String): TokenPair
}
