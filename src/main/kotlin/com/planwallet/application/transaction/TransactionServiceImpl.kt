package com.planwallet.application.transaction

import com.planwallet.domain.category.CategoryType
import com.planwallet.domain.transaction.Transaction
import com.planwallet.domain.transaction.TransactionType
import com.planwallet.infrastructure.category.CategoryRepository
import com.planwallet.infrastructure.transaction.TransactionRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

/**
 * 거래 유스케이스 구현.
 */
@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : TransactionService {
    /**
     * 거래를 생성한다.
     */
    override fun create(
        userId: Long,
        type: TransactionType,
        amount: Long,
        categoryId: Long,
        memo: String?,
        occurredAt: Instant,
    ): Transaction {
        if (amount <= 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive")
        }
        val category = categoryRepository.findByIdAndUserIdAndIsDeletedFalse(categoryId, userId)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found")
        val expectedType = if (type == TransactionType.INCOME) CategoryType.INCOME else CategoryType.EXPENSE
        if (category.type != expectedType) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Category type mismatch")
        }

        val transaction = Transaction(
            userId = userId,
            type = type,
            amount = amount,
            categoryId = categoryId,
            memo = memo,
            occurredAt = occurredAt,
        )
        return transactionRepository.save(transaction)
    }

    /**
     * 거래 목록을 조회한다.
     */
    override fun list(userId: Long): List<Transaction> {
        return transactionRepository.findAllByUserIdOrderByOccurredAtDesc(userId)
    }

    /**
     * 거래를 수정한다.
     */
    override fun update(
        userId: Long,
        transactionId: Long,
        type: TransactionType,
        amount: Long,
        categoryId: Long,
        memo: String?,
        occurredAt: Instant,
    ): Transaction {
        if (amount <= 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive")
        }
        val transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found")
        val category = categoryRepository.findByIdAndUserIdAndIsDeletedFalse(categoryId, userId)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found")
        val expectedType = if (type == TransactionType.INCOME) CategoryType.INCOME else CategoryType.EXPENSE
        if (category.type != expectedType) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Category type mismatch")
        }

        transaction.update(type, amount, categoryId, memo, occurredAt)
        return transactionRepository.save(transaction)
    }

    /**
     * 거래를 삭제한다.
     */
    override fun delete(userId: Long, transactionId: Long) {
        val transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found")
        transactionRepository.delete(transaction)
    }
}
