package com.planwallet.presentation.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.planwallet.application.auth.AuthService
import com.planwallet.domain.auth.TokenPair
import com.planwallet.presentation.auth.dto.LoginRequest
import com.planwallet.presentation.auth.dto.RefreshRequest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

/**
 * AuthController 단위 테스트(MockMvc standalone).
 */
class AuthControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var objectMapper: ObjectMapper

    private val authService = mockk<AuthService>()

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper().findAndRegisterModules()

        val controller = AuthController(authService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `login returns tokens`() {
        every { authService.login("user@example.com", "P@ssw0rd123") } returns TokenPair(
            accessToken = "access",
            refreshToken = "refresh",
        )

        val request = LoginRequest(
            email = "user@example.com",
            password = "P@ssw0rd123",
        )

        mockMvc.perform(
            post("/plan/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("access"))
            .andExpect(jsonPath("$.refreshToken").value("refresh"))
    }

    @Test
    fun `refresh returns new tokens`() {
        every { authService.refresh("refresh") } returns TokenPair(
            accessToken = "access",
            refreshToken = "refresh2",
        )

        val request = RefreshRequest(refreshToken = "refresh")

        mockMvc.perform(
            post("/plan/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("access"))
            .andExpect(jsonPath("$.refreshToken").value("refresh2"))
    }
}
