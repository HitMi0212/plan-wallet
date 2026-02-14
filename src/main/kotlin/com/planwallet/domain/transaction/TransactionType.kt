package com.planwallet.domain.transaction

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 거래 유형.
 */
@Schema(description = "거래 유형")
enum class TransactionType {
    /** 수입 */
    INCOME,

    /** 지출 */
    EXPENSE,
}
