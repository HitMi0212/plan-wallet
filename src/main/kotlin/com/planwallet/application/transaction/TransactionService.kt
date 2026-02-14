package com.planwallet.application.transaction

import com.planwallet.domain.transaction.Transaction
import com.planwallet.domain.transaction.TransactionType
import java.time.Instant

/**
 * 거래 유스케이스 정의.
 */
interface TransactionService {
    /**
     * 거래 생성.
     */
    fun create(
        userId: Long,
        type: TransactionType,
        amount: Long,
        categoryId: Long,
        memo: String?,
        occurredAt: Instant,
    ): Transaction

    /**
     * 거래 목록 조회.
     */
    fun list(userId: Long): List<Transaction>

    /**
     * 거래 수정.
     */
    fun update(
        userId: Long,
        transactionId: Long,
        type: TransactionType,
        amount: Long,
        categoryId: Long,
        memo: String?,
        occurredAt: Instant,
    ): Transaction

    /**
     * 거래 삭제.
     */
    fun delete(userId: Long, transactionId: Long)
}
