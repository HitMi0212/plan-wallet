package com.planwallet.application.stats

import com.planwallet.domain.transaction.Transaction
import com.planwallet.domain.transaction.TransactionType
import com.planwallet.infrastructure.transaction.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

/**
 * StatsServiceImpl 단위 테스트.
 */
class StatsServiceImplTest {
    private val transactionRepository = mockk<TransactionRepository>()

    private val statsService = StatsServiceImpl(transactionRepository)

    @Test
    fun `monthlySummary aggregates income and expense`() {
        val transactions = listOf(
            Transaction(1L, TransactionType.INCOME, 1000, 10L, null, Instant.parse("2025-01-01T00:00:00Z")),
            Transaction(1L, TransactionType.EXPENSE, 200, 11L, null, Instant.parse("2025-01-05T00:00:00Z")),
            Transaction(1L, TransactionType.EXPENSE, 300, 11L, null, Instant.parse("2025-01-06T00:00:00Z")),
        )
        every { transactionRepository.findAllByUserIdAndOccurredAtBetween(any(), any(), any()) } returns transactions

        val summary = statsService.monthlySummary(1L, 2025, 1)

        assertEquals(1000, summary.incomeTotal)
        assertEquals(500, summary.expenseTotal)
    }

    @Test
    fun `categoryTotals aggregates by category`() {
        val transactions = listOf(
            Transaction(1L, TransactionType.EXPENSE, 200, 11L, null, Instant.parse("2025-01-05T00:00:00Z")),
            Transaction(1L, TransactionType.EXPENSE, 300, 11L, null, Instant.parse("2025-01-06T00:00:00Z")),
            Transaction(1L, TransactionType.EXPENSE, 100, 12L, null, Instant.parse("2025-01-07T00:00:00Z")),
        )
        every { transactionRepository.findAllByUserIdAndOccurredAtBetween(any(), any(), any()) } returns transactions

        val totals = statsService.categoryTotals(1L, LocalDate.parse("2025-01-01"), LocalDate.parse("2025-01-31"))

        assertEquals(2, totals.size)
        assertEquals(11L, totals[0].categoryId)
        assertEquals(500, totals[0].total)
    }
}
