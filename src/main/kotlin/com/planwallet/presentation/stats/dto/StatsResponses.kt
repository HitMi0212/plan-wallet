package com.planwallet.presentation.stats.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 월간 통계 응답 DTO.
 */
@Schema(description = "월간 통계 응답")
data class MonthlySummaryResponse(
    @field:Schema(description = "연도", example = "2025")
    val year: Int,
    @field:Schema(description = "월", example = "1")
    val month: Int,
    @field:Schema(description = "수입 합계", example = "2500000")
    val incomeTotal: Long,
    @field:Schema(description = "지출 합계", example = "120000")
    val expenseTotal: Long,
)

/**
 * 카테고리별 합계 응답 DTO.
 */
@Schema(description = "카테고리별 합계 응답")
data class CategoryTotalResponse(
    @field:Schema(description = "카테고리 ID", example = "10")
    val categoryId: Long,
    @field:Schema(description = "합계", example = "45000")
    val total: Long,
)
