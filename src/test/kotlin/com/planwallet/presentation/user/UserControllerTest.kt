package com.planwallet.presentation.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.planwallet.application.user.UserService
import com.planwallet.domain.user.User
import com.planwallet.presentation.user.dto.SignUpRequest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

/**
 * UserController 단위 테스트(MockMvc standalone).
 */
class UserControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var objectMapper: ObjectMapper

    private val userService = mockk<UserService>()

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper().findAndRegisterModules()

        val controller = UserController(userService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
            .build()
    }

    @Test
    fun `sign up returns created user`() {
        val user = User(
            email = "user@example.com",
            passwordHash = "hash",
            nickname = "tester",
        ).apply {
            id = 1L
            onCreate()
        }

        every { userService.register("user@example.com", "P@ssw0rd123", "tester") } returns user

        val request = SignUpRequest(
            email = "user@example.com",
            password = "P@ssw0rd123",
            nickname = "tester",
        )

        mockMvc.perform(
            post("/plan/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("user@example.com"))
            .andExpect(jsonPath("$.nickname").value("tester"))
    }

    @Test
    fun `me returns current user`() {
        val user = User(
            email = "user@example.com",
            passwordHash = "hash",
            nickname = "tester",
        ).apply {
            id = 1L
            onCreate()
        }

        every { userService.getById(1L) } returns user

        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(get("/plan/users/me").principal(principal))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("user@example.com"))
    }
}
