package com.planwallet.presentation.transaction.dto

import com.planwallet.domain.transaction.TransactionType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.Instant

/**
 * 거래 생성 요청 DTO.
 */
@Schema(description = "거래 생성 요청")
data class TransactionCreateRequest(
    @field:NotNull
    @field:Schema(description = "거래 유형", example = "EXPENSE")
    val type: TransactionType,
    @field:Min(1)
    @field:Schema(description = "거래 금액", example = "12000")
    val amount: Long,
    @field:NotNull
    @field:Schema(description = "카테고리 ID", example = "10")
    val categoryId: Long,
    @field:Schema(description = "메모", example = "점심")
    val memo: String? = null,
    @field:NotNull
    @field:Schema(description = "거래 발생 일시")
    val occurredAt: Instant,
)
