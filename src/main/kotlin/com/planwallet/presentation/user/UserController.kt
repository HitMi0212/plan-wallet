package com.planwallet.presentation.user

import com.planwallet.application.user.UserService
import com.planwallet.presentation.user.dto.SignUpRequest
import com.planwallet.presentation.user.dto.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * 사용자 관련 API 컨트롤러.
 */
@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/plan/users")
class UserController(
    private val userService: UserService,
) {
    /**
     * 회원 가입 API.
     */
    @Operation(summary = "회원 가입", description = "이메일/비밀번호/닉네임으로 회원 가입합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@Valid @RequestBody request: SignUpRequest): UserResponse {
        val user = userService.register(request.email, request.password, request.nickname)
        return UserResponse(
            id = user.id ?: 0,
            email = user.email,
            nickname = user.nickname,
            createdAt = user.createdAt ?: throw IllegalStateException("createdAt not set"),
        )
    }

    /**
     * 로그인 사용자 정보 조회 API.
     */
    @Operation(summary = "내 정보", description = "로그인 사용자 정보를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    fun me(authentication: Authentication): UserResponse {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        val user = userService.getById(userId)
        return UserResponse(
            id = user.id ?: 0,
            email = user.email,
            nickname = user.nickname,
            createdAt = user.createdAt ?: throw IllegalStateException("createdAt not set"),
        )
    }
}
