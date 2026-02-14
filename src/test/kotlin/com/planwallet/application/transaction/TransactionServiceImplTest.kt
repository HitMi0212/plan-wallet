package com.planwallet.application.transaction

import com.planwallet.domain.category.Category
import com.planwallet.domain.category.CategoryType
import com.planwallet.domain.transaction.Transaction
import com.planwallet.domain.transaction.TransactionType
import com.planwallet.infrastructure.category.CategoryRepository
import com.planwallet.infrastructure.transaction.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

/**
 * TransactionServiceImpl 단위 테스트.
 */
class TransactionServiceImplTest {
    private val transactionRepository = mockk<TransactionRepository>()
    private val categoryRepository = mockk<CategoryRepository>()

    private val transactionService = TransactionServiceImpl(
        transactionRepository = transactionRepository,
        categoryRepository = categoryRepository,
    )

    @Test
    fun `create saves transaction when valid`() {
        val category = Category(userId = 1L, type = CategoryType.EXPENSE, name = "식비").apply { id = 10L }
        every { categoryRepository.findByIdAndUserIdAndIsDeletedFalse(10L, 1L) } returns category
        every { transactionRepository.save(any()) } answers { firstArg() }

        val result = transactionService.create(
            userId = 1L,
            type = TransactionType.EXPENSE,
            amount = 12000,
            categoryId = 10L,
            memo = "점심",
            occurredAt = Instant.parse("2025-01-01T00:00:00Z"),
        )

        assertEquals(12000, result.amount)
        verify(exactly = 1) { transactionRepository.save(any()) }
    }

    @Test
    fun `create throws bad request when amount invalid`() {
        val exception = assertThrows(ResponseStatusException::class.java) {
            transactionService.create(
                userId = 1L,
                type = TransactionType.EXPENSE,
                amount = 0,
                categoryId = 10L,
                memo = null,
                occurredAt = Instant.parse("2025-01-01T00:00:00Z"),
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    }

    @Test
    fun `update throws not found when transaction missing`() {
        every { transactionRepository.findByIdAndUserId(1L, 1L) } returns null

        val exception = assertThrows(ResponseStatusException::class.java) {
            transactionService.update(
                userId = 1L,
                transactionId = 1L,
                type = TransactionType.EXPENSE,
                amount = 1000,
                categoryId = 10L,
                memo = null,
                occurredAt = Instant.parse("2025-01-01T00:00:00Z"),
            )
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
    }

    @Test
    fun `list returns transactions`() {
        val transactions = listOf(
            Transaction(userId = 1L, type = TransactionType.EXPENSE, amount = 12000, categoryId = 10L, memo = null, occurredAt = Instant.now()),
            Transaction(userId = 1L, type = TransactionType.INCOME, amount = 2000000, categoryId = 11L, memo = null, occurredAt = Instant.now()),
        )
        every { transactionRepository.findAllByUserIdOrderByOccurredAtDesc(1L) } returns transactions

        val result = transactionService.list(1L)

        assertEquals(2, result.size)
    }

    @Test
    fun `delete removes transaction`() {
        val transaction = Transaction(userId = 1L, type = TransactionType.EXPENSE, amount = 12000, categoryId = 10L, memo = null, occurredAt = Instant.now())
        every { transactionRepository.findByIdAndUserId(1L, 1L) } returns transaction
        every { transactionRepository.delete(transaction) } returns Unit

        transactionService.delete(1L, 1L)

        verify(exactly = 1) { transactionRepository.delete(transaction) }
    }
}
