package com.planwallet.domain.category

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 카테고리 유형.
 */
@Schema(description = "카테고리 유형")
enum class CategoryType {
    /** 수입 */
    INCOME,

    /** 지출 */
    EXPENSE,
}
