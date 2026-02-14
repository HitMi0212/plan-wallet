package com.planwallet.infrastructure.category

import com.planwallet.domain.category.Category
import com.planwallet.domain.category.CategoryType
import org.springframework.data.jpa.repository.JpaRepository

/**
 * 카테고리 JPA 리포지토리.
 */
interface CategoryRepository : JpaRepository<Category, Long> {
    /**
     * 사용자별 활성 카테고리 목록.
     */
    fun findAllByUserIdAndIsDeletedFalse(userId: Long): List<Category>

    /**
     * 사용자별 활성 카테고리 단건 조회.
     */
    fun findByIdAndUserIdAndIsDeletedFalse(id: Long, userId: Long): Category?

    /**
     * 동일 유형/이름 카테고리 중복 여부.
     */
    fun existsByUserIdAndTypeAndNameAndIsDeletedFalse(userId: Long, type: CategoryType, name: String): Boolean
}
