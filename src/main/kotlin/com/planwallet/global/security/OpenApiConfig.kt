package com.planwallet.global.security

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

/**
 * OpenAPI 기본 설정.
 */
@OpenAPIDefinition(
    info = Info(
        title = "plan-wallet API",
        version = "v1",
        description = "plan-wallet backend API documentation",
    ),
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
)
@Configuration
class OpenApiConfig
