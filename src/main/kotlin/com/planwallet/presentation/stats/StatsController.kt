package com.planwallet.presentation.stats

import com.planwallet.application.stats.StatsService
import com.planwallet.presentation.stats.dto.CategoryTotalResponse
import com.planwallet.presentation.stats.dto.MonthlySummaryResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

/**
 * 통계 API 컨트롤러.
 */
@Tag(name = "Stats", description = "통계 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/plan/stats")
class StatsController(
    private val statsService: StatsService,
) {
    /**
     * 월간 수입/지출 합계.
     */
    @Operation(summary = "월간 합계", description = "지정 월의 수입/지출 합계를 반환합니다.")
    @GetMapping("/monthly")
    fun monthly(
        authentication: Authentication,
        @RequestParam("year") year: Int,
        @RequestParam("month") @Min(1) @Max(12) month: Int,
    ): MonthlySummaryResponse {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        val summary = statsService.monthlySummary(userId, year, month)
        return MonthlySummaryResponse(
            year = summary.year,
            month = summary.month,
            incomeTotal = summary.incomeTotal,
            expenseTotal = summary.expenseTotal,
        )
    }

    /**
     * 카테고리별 합계.
     */
    @Operation(summary = "카테고리별 합계", description = "기간 내 카테고리별 합계를 반환합니다.")
    @GetMapping("/categories")
    fun categoryTotals(
        authentication: Authentication,
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate,
    ): List<CategoryTotalResponse> {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        return statsService.categoryTotals(userId, from, to).map {
            CategoryTotalResponse(categoryId = it.categoryId, total = it.total)
        }
    }
}
