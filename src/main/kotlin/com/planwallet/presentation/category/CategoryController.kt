package com.planwallet.presentation.category

import com.planwallet.application.category.CategoryService
import com.planwallet.presentation.category.dto.CategoryCreateRequest
import com.planwallet.presentation.category.dto.CategoryResponse
import com.planwallet.presentation.category.dto.CategoryUpdateRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * 카테고리 API 컨트롤러.
 */
@Tag(name = "Category", description = "카테고리 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/plan/categories")
class CategoryController(
    private val categoryService: CategoryService,
) {
    /**
     * 카테고리 생성.
     */
    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        authentication: Authentication,
        @Valid @RequestBody request: CategoryCreateRequest,
    ): CategoryResponse {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        val category = categoryService.create(userId, request.type, request.name)
        return CategoryResponse(
            id = category.id ?: 0,
            type = category.type,
            name = category.name,
            createdAt = category.createdAt ?: throw IllegalStateException("createdAt not set"),
            updatedAt = category.updatedAt ?: throw IllegalStateException("updatedAt not set"),
        )
    }

    /**
     * 카테고리 목록 조회.
     */
    @Operation(summary = "카테고리 목록", description = "카테고리 목록을 조회합니다.")
    @GetMapping
    fun list(authentication: Authentication): List<CategoryResponse> {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        return categoryService.list(userId).map { category ->
            CategoryResponse(
                id = category.id ?: 0,
                type = category.type,
                name = category.name,
                createdAt = category.createdAt ?: throw IllegalStateException("createdAt not set"),
                updatedAt = category.updatedAt ?: throw IllegalStateException("updatedAt not set"),
            )
        }
    }

    /**
     * 카테고리 수정.
     */
    @Operation(summary = "카테고리 수정", description = "카테고리 이름을 수정합니다.")
    @PatchMapping("/{id}")
    fun update(
        authentication: Authentication,
        @PathVariable("id") id: Long,
        @Valid @RequestBody request: CategoryUpdateRequest,
    ): CategoryResponse {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        val category = categoryService.update(userId, id, request.name)
        return CategoryResponse(
            id = category.id ?: 0,
            type = category.type,
            name = category.name,
            createdAt = category.createdAt ?: throw IllegalStateException("createdAt not set"),
            updatedAt = category.updatedAt ?: throw IllegalStateException("updatedAt not set"),
        )
    }

    /**
     * 카테고리 삭제(soft delete).
     */
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제 처리합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(authentication: Authentication, @PathVariable("id") id: Long) {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        categoryService.delete(userId, id)
    }
}
