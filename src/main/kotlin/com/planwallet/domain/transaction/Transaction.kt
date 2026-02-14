package com.planwallet.domain.transaction

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.Instant

/**
 * 거래 엔티티.
 */
@Schema(description = "거래 엔티티")
@Entity
@Table(name = "transactions")
class Transaction(
    /** 사용자 ID */
    @field:Schema(description = "사용자 ID", example = "1")
    @Column(nullable = false)
    var userId: Long,

    /** 거래 유형(수입/지출) */
    @field:Schema(description = "거래 유형", example = "EXPENSE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: TransactionType,

    /** 거래 금액(원 단위) */
    @field:Schema(description = "거래 금액", example = "12000")
    @Column(nullable = false)
    var amount: Long,

    /** 카테고리 ID */
    @field:Schema(description = "카테고리 ID", example = "10")
    @Column(nullable = false)
    var categoryId: Long,

    /** 메모 */
    @field:Schema(description = "메모", example = "점심")
    @Column(nullable = true)
    var memo: String? = null,

    /** 거래 발생 일시 */
    @field:Schema(description = "거래 발생 일시")
    @Column(nullable = false)
    var occurredAt: Instant,
) {
    /** 거래 ID */
    @field:Schema(description = "거래 ID", example = "100")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /** 생성 시각 */
    @field:Schema(description = "생성 시각")
    @Column(nullable = false)
    var createdAt: Instant? = null
        private set

    /** 수정 시각 */
    @field:Schema(description = "수정 시각")
    @Column(nullable = false)
    var updatedAt: Instant? = null
        private set

    /** 메모 변경 */
    fun changeMemo(newMemo: String?) {
        memo = newMemo
    }

    /** 거래 정보 변경 */
    fun update(
        newType: TransactionType,
        newAmount: Long,
        newCategoryId: Long,
        newMemo: String?,
        newOccurredAt: Instant,
    ) {
        type = newType
        amount = newAmount
        categoryId = newCategoryId
        memo = newMemo
        occurredAt = newOccurredAt
    }

    @PrePersist
    fun onCreate() {
        val now = Instant.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}
