package com.planwallet.infrastructure.transaction

import com.planwallet.domain.transaction.Transaction
import org.springframework.data.jpa.repository.JpaRepository

/**
 * 거래 JPA 리포지토리.
 */
interface TransactionRepository : JpaRepository<Transaction, Long> {
    /**
     * 사용자별 거래 목록 조회.
     */
    fun findAllByUserIdOrderByOccurredAtDesc(userId: Long): List<Transaction>

    /**
     * 사용자별 거래 단건 조회.
     */
    fun findByIdAndUserId(id: Long, userId: Long): Transaction?
}
