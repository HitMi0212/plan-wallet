package com.planwallet.application.category

import com.planwallet.domain.category.Category
import com.planwallet.domain.category.CategoryType

/**
 * 카테고리 유스케이스 정의.
 */
interface CategoryService {
    /**
     * 카테고리 생성.
     */
    fun create(userId: Long, type: CategoryType, name: String): Category

    /**
     * 카테고리 목록 조회.
     */
    fun list(userId: Long): List<Category>

    /**
     * 카테고리 이름 변경.
     */
    fun update(userId: Long, categoryId: Long, name: String): Category

    /**
     * 카테고리 삭제(soft delete).
     */
    fun delete(userId: Long, categoryId: Long)
}
