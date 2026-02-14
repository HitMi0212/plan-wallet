package com.planwallet.global.security

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties("jwt")
data class JwtProperties(
    @field:NotBlank
    val issuer: String,
    @field:NotBlank
    val secret: String,
    @field:Min(1)
    val accessTokenMinutes: Long,
    @field:Min(1)
    val refreshTokenDays: Long,
)
