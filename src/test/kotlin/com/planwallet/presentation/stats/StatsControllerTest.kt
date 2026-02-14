package com.planwallet.presentation.stats

import com.planwallet.application.stats.CategoryTotal
import com.planwallet.application.stats.MonthlyComparison
import com.planwallet.application.stats.MonthlySummary
import com.planwallet.application.stats.StatsService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate

/**
 * StatsController 단위 테스트(MockMvc standalone).
 */
class StatsControllerTest {
    private lateinit var mockMvc: MockMvc

    private val statsService = mockk<StatsService>()

    @BeforeEach
    fun setUp() {
        val controller = StatsController(statsService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `monthly returns summary`() {
        every { statsService.monthlySummary(1L, 2025, 1) } returns MonthlySummary(2025, 1, 1000, 500)

        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(get("/plan/stats/monthly?year=2025&month=1").principal(principal))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.incomeTotal").value(1000))
            .andExpect(jsonPath("$.expenseTotal").value(500))
    }

    @Test
    fun `monthly compare returns summary`() {
        every { statsService.monthlyComparison(1L, 2025, 1) } returns MonthlyComparison(2025, 1, 1000, 500, 800, 400)

        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(get("/plan/stats/monthly/compare?year=2025&month=1").principal(principal))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.incomeTotal").value(1000))
            .andExpect(jsonPath("$.prevIncomeTotal").value(800))
    }

    @Test
    fun `category totals returns list`() {
        every {
            statsService.categoryTotals(
                1L,
                LocalDate.parse("2025-01-01"),
                LocalDate.parse("2025-01-31"),
            )
        } returns listOf(
            CategoryTotal(10L, 500),
            CategoryTotal(11L, 200),
        )

        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(get("/plan/stats/categories?from=2025-01-01&to=2025-01-31").principal(principal))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].categoryId").value(10))
            .andExpect(jsonPath("$[0].total").value(500))
    }
}
