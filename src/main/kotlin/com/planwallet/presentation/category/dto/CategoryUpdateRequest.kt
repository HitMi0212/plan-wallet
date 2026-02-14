package com.planwallet.presentation.category.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 카테고리 수정 요청 DTO.
 */
@Schema(description = "카테고리 수정 요청")
data class CategoryUpdateRequest(
    @field:NotBlank
    @field:Schema(description = "카테고리 이름", example = "생활비")
    val name: String,
)
