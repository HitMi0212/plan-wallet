package com.planwallet.application.stats

import java.time.LocalDate

/**
 * 월간 통계 응답 모델.
 */
data class MonthlySummary(
    val year: Int,
    val month: Int,
    val incomeTotal: Long,
    val expenseTotal: Long,
)

/**
 * 카테고리별 합계 모델.
 */
data class CategoryTotal(
    val categoryId: Long,
    val total: Long,
)

/**
 * 월간 비교 응답 모델.
 */
data class MonthlyComparison(
    val year: Int,
    val month: Int,
    val incomeTotal: Long,
    val expenseTotal: Long,
    val prevIncomeTotal: Long,
    val prevExpenseTotal: Long,
)

/**
 * 통계 유스케이스 정의.
 */
interface StatsService {
    /**
     * 월간 수입/지출 합계.
     */
    fun monthlySummary(userId: Long, year: Int, month: Int): MonthlySummary

    /**
     * 카테고리별 합계.
     */
    fun categoryTotals(userId: Long, from: LocalDate, to: LocalDate): List<CategoryTotal>

    /**
     * 전월 대비 월간 비교.
     */
    fun monthlyComparison(userId: Long, year: Int, month: Int): MonthlyComparison
}
