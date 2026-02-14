package com.planwallet.application.user

import com.planwallet.domain.user.User
import com.planwallet.infrastructure.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.server.ResponseStatusException
import java.util.Optional

/**
 * UserServiceImpl 단위 테스트.
 */
class UserServiceImplTest {
    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()

    private val userService = UserServiceImpl(
        userRepository = userRepository,
        passwordEncoder = passwordEncoder,
    )

    @Test
    fun `register creates user when email is unique`() {
        every { userRepository.existsByEmail("user@example.com") } returns false
        every { passwordEncoder.encode("P@ssw0rd123") } returns "hash"
        every { userRepository.save(any()) } answers { firstArg() }

        val result = userService.register("user@example.com", "P@ssw0rd123", "tester")

        assertEquals("user@example.com", result.email)
        assertEquals("hash", result.passwordHash)
        assertEquals("tester", result.nickname)
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `register throws conflict when email exists`() {
        every { userRepository.existsByEmail("user@example.com") } returns true

        val exception = assertThrows(ResponseStatusException::class.java) {
            userService.register("user@example.com", "P@ssw0rd123", "tester")
        }

        assertEquals(HttpStatus.CONFLICT, exception.statusCode)
    }

    @Test
    fun `getById returns user when found`() {
        val user = User(
            email = "user@example.com",
            passwordHash = "hash",
            nickname = "tester",
        ).apply { id = 1L }

        every { userRepository.findById(1L) } returns Optional.of(user)

        val result = userService.getById(1L)

        assertEquals(1L, result.id)
    }

    @Test
    fun `getById throws not found when missing`() {
        every { userRepository.findById(1L) } returns Optional.empty()

        val exception = assertThrows(ResponseStatusException::class.java) {
            userService.getById(1L)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
    }
}
