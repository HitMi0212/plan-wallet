package com.planwallet.application.category

import com.planwallet.domain.category.Category
import com.planwallet.domain.category.CategoryType
import com.planwallet.infrastructure.category.CategoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * CategoryServiceImpl 단위 테스트.
 */
class CategoryServiceImplTest {
    private val categoryRepository = mockk<CategoryRepository>()

    private val categoryService = CategoryServiceImpl(categoryRepository)

    @Test
    fun `create saves category when unique`() {
        every { categoryRepository.existsByUserIdAndTypeAndNameAndIsDeletedFalse(1L, CategoryType.EXPENSE, "식비") } returns false
        every { categoryRepository.save(any()) } answers { firstArg() }

        val result = categoryService.create(1L, CategoryType.EXPENSE, "식비")

        assertEquals(1L, result.userId)
        assertEquals(CategoryType.EXPENSE, result.type)
        assertEquals("식비", result.name)
        verify(exactly = 1) { categoryRepository.save(any()) }
    }

    @Test
    fun `create throws conflict when duplicated`() {
        every { categoryRepository.existsByUserIdAndTypeAndNameAndIsDeletedFalse(1L, CategoryType.EXPENSE, "식비") } returns true

        val exception = assertThrows(ResponseStatusException::class.java) {
            categoryService.create(1L, CategoryType.EXPENSE, "식비")
        }

        assertEquals(HttpStatus.CONFLICT, exception.statusCode)
    }

    @Test
    fun `list returns categories`() {
        val categories = listOf(
            Category(userId = 1L, type = CategoryType.EXPENSE, name = "식비"),
            Category(userId = 1L, type = CategoryType.INCOME, name = "급여"),
        )
        every { categoryRepository.findAllByUserIdAndIsDeletedFalse(1L) } returns categories

        val result = categoryService.list(1L)

        assertEquals(2, result.size)
    }

    @Test
    fun `update changes category name`() {
        val category = Category(userId = 1L, type = CategoryType.EXPENSE, name = "식비").apply { id = 10L }
        every { categoryRepository.findByIdAndUserIdAndIsDeletedFalse(10L, 1L) } returns category
        every { categoryRepository.save(any()) } answers { firstArg() }

        val result = categoryService.update(1L, 10L, "외식")

        assertEquals("외식", result.name)
    }

    @Test
    fun `update throws not found when missing`() {
        every { categoryRepository.findByIdAndUserIdAndIsDeletedFalse(10L, 1L) } returns null

        val exception = assertThrows(ResponseStatusException::class.java) {
            categoryService.update(1L, 10L, "외식")
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
    }

    @Test
    fun `delete marks category as deleted`() {
        val category = Category(userId = 1L, type = CategoryType.EXPENSE, name = "식비").apply { id = 10L }
        every { categoryRepository.findByIdAndUserIdAndIsDeletedFalse(10L, 1L) } returns category
        every { categoryRepository.save(any()) } answers { firstArg() }

        categoryService.delete(1L, 10L)

        assertEquals(true, category.isDeleted)
    }
}
