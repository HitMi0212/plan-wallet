package com.planwallet.application.stats

import com.planwallet.domain.transaction.TransactionType
import com.planwallet.infrastructure.transaction.TransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * 통계 유스케이스 구현.
 */
@Service
class StatsServiceImpl(
    private val transactionRepository: TransactionRepository,
) : StatsService {
    /**
     * 월간 수입/지출 합계를 계산한다.
     */
    override fun monthlySummary(userId: Long, year: Int, month: Int): MonthlySummary {
        val start = LocalDate.of(year, month, 1).atStartOfDay().toInstant(ZoneOffset.UTC)
        val end = LocalDate.of(year, month, 1)
            .plusMonths(1)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)

        val transactions = transactionRepository.findAllByUserIdAndOccurredAtBetween(userId, start, end)

        val income = transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
        val expense = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        return MonthlySummary(
            year = year,
            month = month,
            incomeTotal = income,
            expenseTotal = expense,
        )
    }

    /**
     * 기간 내 카테고리별 합계를 계산한다.
     */
    override fun categoryTotals(userId: Long, from: LocalDate, to: LocalDate): List<CategoryTotal> {
        val start = from.atStartOfDay().toInstant(ZoneOffset.UTC)
        val end = to.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)

        val transactions = transactionRepository.findAllByUserIdAndOccurredAtBetween(userId, start, end)

        return transactions
            .groupBy { it.categoryId }
            .map { (categoryId, items) ->
                CategoryTotal(categoryId, items.sumOf { it.amount })
            }
            .sortedByDescending { it.total }
    }
}
