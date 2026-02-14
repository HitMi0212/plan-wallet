package com.planwallet.presentation.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import com.planwallet.application.transaction.TransactionService
import com.planwallet.domain.transaction.Transaction
import com.planwallet.domain.transaction.TransactionType
import com.planwallet.presentation.transaction.dto.TransactionCreateRequest
import com.planwallet.presentation.transaction.dto.TransactionUpdateRequest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.Instant

/**
 * TransactionController 단위 테스트(MockMvc standalone).
 */
class TransactionControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var objectMapper: ObjectMapper

    private val transactionService = mockk<TransactionService>()

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper().findAndRegisterModules()

        val controller = TransactionController(transactionService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `create returns created transaction`() {
        val transaction = Transaction(
            userId = 1L,
            type = TransactionType.EXPENSE,
            amount = 12000,
            categoryId = 10L,
            memo = "점심",
            occurredAt = Instant.parse("2025-01-01T00:00:00Z"),
        ).apply {
            id = 100L
            onCreate()
        }

        every {
            transactionService.create(
                userId = 1L,
                type = TransactionType.EXPENSE,
                amount = 12000,
                categoryId = 10L,
                memo = "점심",
                occurredAt = Instant.parse("2025-01-01T00:00:00Z"),
            )
        } returns transaction

        val request = TransactionCreateRequest(
            type = TransactionType.EXPENSE,
            amount = 12000,
            categoryId = 10L,
            memo = "점심",
            occurredAt = Instant.parse("2025-01-01T00:00:00Z"),
        )
        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(
            post("/plan/transactions")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.amount").value(12000))
    }

    @Test
    fun `list returns transactions`() {
        val transactions = listOf(
            Transaction(
                userId = 1L,
                type = TransactionType.EXPENSE,
                amount = 12000,
                categoryId = 10L,
                memo = null,
                occurredAt = Instant.parse("2025-01-01T00:00:00Z"),
            ).apply { id = 1L; onCreate() },
            Transaction(
                userId = 1L,
                type = TransactionType.INCOME,
                amount = 2000000,
                categoryId = 11L,
                memo = null,
                occurredAt = Instant.parse("2025-02-01T00:00:00Z"),
            ).apply { id = 2L; onCreate() },
        )
        every { transactionService.list(1L) } returns transactions

        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(get("/plan/transactions").principal(principal))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
    }

    @Test
    fun `update returns updated transaction`() {
        val transaction = Transaction(
            userId = 1L,
            type = TransactionType.EXPENSE,
            amount = 15000,
            categoryId = 10L,
            memo = "외식",
            occurredAt = Instant.parse("2025-01-02T00:00:00Z"),
        ).apply { id = 100L; onCreate() }

        every {
            transactionService.update(
                userId = 1L,
                transactionId = 100L,
                type = TransactionType.EXPENSE,
                amount = 15000,
                categoryId = 10L,
                memo = "외식",
                occurredAt = Instant.parse("2025-01-02T00:00:00Z"),
            )
        } returns transaction

        val request = TransactionUpdateRequest(
            type = TransactionType.EXPENSE,
            amount = 15000,
            categoryId = 10L,
            memo = "외식",
            occurredAt = Instant.parse("2025-01-02T00:00:00Z"),
        )
        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(
            patch("/plan/transactions/100")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.amount").value(15000))
    }

    @Test
    fun `delete returns no content`() {
        every { transactionService.delete(1L, 100L) } returns Unit

        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(delete("/plan/transactions/100").principal(principal))
            .andExpect(status().isNoContent)
    }
}
