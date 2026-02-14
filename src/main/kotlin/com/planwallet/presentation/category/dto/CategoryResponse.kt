package com.planwallet.presentation.category.dto

import com.planwallet.domain.category.CategoryType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * 카테고리 응답 DTO.
 */
@Schema(description = "카테고리 응답")
data class CategoryResponse(
    @field:Schema(description = "카테고리 ID", example = "1")
    val id: Long,
    @field:Schema(description = "카테고리 유형", example = "EXPENSE")
    val type: CategoryType,
    @field:Schema(description = "카테고리 이름", example = "식비")
    val name: String,
    @field:Schema(description = "생성 시각")
    val createdAt: Instant,
    @field:Schema(description = "수정 시각")
    val updatedAt: Instant,
)
