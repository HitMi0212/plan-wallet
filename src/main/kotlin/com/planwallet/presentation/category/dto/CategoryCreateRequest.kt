package com.planwallet.presentation.category.dto

import com.planwallet.domain.category.CategoryType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * 카테고리 생성 요청 DTO.
 */
@Schema(description = "카테고리 생성 요청")
data class CategoryCreateRequest(
    @field:NotNull
    @field:Schema(description = "카테고리 유형", example = "EXPENSE")
    val type: CategoryType,
    @field:NotBlank
    @field:Schema(description = "카테고리 이름", example = "식비")
    val name: String,
)
