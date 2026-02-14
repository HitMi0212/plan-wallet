package com.planwallet.global.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

/**
 * JWT 생성/검증을 담당하는 컴포넌트.
 */
@Component
class JwtTokenProvider(
    private val properties: JwtProperties,
) {
    private val key = Keys.hmacShaKeyFor(properties.secret.toByteArray(StandardCharsets.UTF_8))

    /**
     * 액세스 토큰 생성.
     */
    fun createAccessToken(subject: String): String {
        return createToken(subject, TokenType.ACCESS)
    }

    /**
     * 리프레시 토큰 생성.
     */
    fun createRefreshToken(subject: String): String {
        return createToken(subject, TokenType.REFRESH)
    }

    /**
     * 토큰에서 subject를 추출한다.
     */
    fun getSubject(token: String): String = parseClaims(token).subject

    /**
     * 토큰 기반 Authentication 생성.
     */
    fun getAuthentication(token: String): Authentication {
        val claims = parseClaims(token)
        val subject = claims.subject
        val authorities = claims["roles"]?.let { roles ->
            when (roles) {
                is String -> roles.split(',').filter { it.isNotBlank() }.map { SimpleGrantedAuthority(it.trim()) }
                is Collection<*> -> roles.filterIsInstance<String>().map { SimpleGrantedAuthority(it) }
                else -> emptyList()
            }
        } ?: emptyList()

        return UsernamePasswordAuthenticationToken(subject, token, authorities)
    }

    /**
     * 리프레시 토큰 여부 확인.
     */
    fun isRefreshToken(token: String): Boolean = getTokenType(token) == TokenType.REFRESH

    /**
     * 토큰 유효성 검사.
     */
    fun isValid(token: String): Boolean {
        return try {
            parseClaims(token)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun createToken(subject: String, type: TokenType): String {
        val now = Instant.now()
        val expiresAt = when (type) {
            TokenType.ACCESS -> now.plus(properties.accessTokenMinutes, ChronoUnit.MINUTES)
            TokenType.REFRESH -> now.plus(properties.refreshTokenDays, ChronoUnit.DAYS)
        }

        return Jwts.builder()
            .issuer(properties.issuer)
            .subject(subject)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .claim("typ", type.value)
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    private fun getTokenType(token: String): TokenType? {
        val type = parseClaims(token)["typ"]?.toString()?.lowercase()
        return TokenType.entries.firstOrNull { it.value == type }
    }

    private fun parseClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private enum class TokenType(val value: String) {
        ACCESS("access"),
        REFRESH("refresh"),
    }
}
