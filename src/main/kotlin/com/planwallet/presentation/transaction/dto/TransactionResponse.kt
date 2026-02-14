package com.planwallet.presentation.transaction.dto

import com.planwallet.domain.transaction.TransactionType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * 거래 응답 DTO.
 */
@Schema(description = "거래 응답")
data class TransactionResponse(
    @field:Schema(description = "거래 ID", example = "100")
    val id: Long,
    @field:Schema(description = "거래 유형", example = "EXPENSE")
    val type: TransactionType,
    @field:Schema(description = "거래 금액", example = "12000")
    val amount: Long,
    @field:Schema(description = "카테고리 ID", example = "10")
    val categoryId: Long,
    @field:Schema(description = "메모", example = "점심")
    val memo: String?,
    @field:Schema(description = "거래 발생 일시")
    val occurredAt: Instant,
    @field:Schema(description = "생성 시각")
    val createdAt: Instant,
    @field:Schema(description = "수정 시각")
    val updatedAt: Instant,
)
