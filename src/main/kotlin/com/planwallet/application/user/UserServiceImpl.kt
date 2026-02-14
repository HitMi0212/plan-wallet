package com.planwallet.application.user

import com.planwallet.domain.user.User
import com.planwallet.infrastructure.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

/**
 * 사용자 관련 유스케이스 구현.
 */
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserService {
    /**
     * 이메일 중복을 확인하고 사용자 등록을 수행한다.
     */
    override fun register(email: String, password: String, nickname: String): User {
        if (userRepository.existsByEmail(email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Email already in use")
        }

        val encodedPassword = passwordEncoder.encode(password)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password encoding failed")

        val user = User(
            email = email,
            passwordHash = encodedPassword,
            nickname = nickname,
        )

        return userRepository.save(user)
    }

    /**
     * ID로 사용자를 조회한다.
     */
    override fun getById(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
    }
}
