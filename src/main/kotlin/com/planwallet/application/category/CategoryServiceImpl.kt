package com.planwallet.application.category

import com.planwallet.domain.category.Category
import com.planwallet.domain.category.CategoryType
import com.planwallet.infrastructure.category.CategoryRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

/**
 * 카테고리 유스케이스 구현.
 */
@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
) : CategoryService {
    /**
     * 사용자별 카테고리를 생성한다.
     */
    override fun create(userId: Long, type: CategoryType, name: String): Category {
        if (categoryRepository.existsByUserIdAndTypeAndNameAndIsDeletedFalse(userId, type, name)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Category already exists")
        }

        val category = Category(
            userId = userId,
            type = type,
            name = name,
        )

        return categoryRepository.save(category)
    }

    /**
     * 사용자별 카테고리 목록을 반환한다.
     */
    override fun list(userId: Long): List<Category> {
        return categoryRepository.findAllByUserIdAndIsDeletedFalse(userId)
    }

    /**
     * 카테고리 이름을 변경한다.
     */
    override fun update(userId: Long, categoryId: Long, name: String): Category {
        val category = categoryRepository.findByIdAndUserIdAndIsDeletedFalse(categoryId, userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")

        category.rename(name)
        return categoryRepository.save(category)
    }

    /**
     * 카테고리를 삭제 처리한다.
     */
    override fun delete(userId: Long, categoryId: Long) {
        val category = categoryRepository.findByIdAndUserIdAndIsDeletedFalse(categoryId, userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")

        category.markDeleted()
        categoryRepository.save(category)
    }
}
