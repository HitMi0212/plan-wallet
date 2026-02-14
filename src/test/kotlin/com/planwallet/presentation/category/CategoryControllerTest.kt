package com.planwallet.presentation.category

import com.fasterxml.jackson.databind.ObjectMapper
import com.planwallet.application.category.CategoryService
import com.planwallet.domain.category.Category
import com.planwallet.domain.category.CategoryType
import com.planwallet.presentation.category.dto.CategoryCreateRequest
import com.planwallet.presentation.category.dto.CategoryUpdateRequest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

/**
 * CategoryController 단위 테스트(MockMvc standalone).
 */
class CategoryControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var objectMapper: ObjectMapper

    private val categoryService = mockk<CategoryService>()

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper().findAndRegisterModules()

        val controller = CategoryController(categoryService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `create returns created category`() {
        val category = Category(userId = 1L, type = CategoryType.EXPENSE, name = "식비").apply {
            id = 10L
            onCreate()
        }

        every { categoryService.create(1L, CategoryType.EXPENSE, "식비") } returns category

        val request = CategoryCreateRequest(type = CategoryType.EXPENSE, name = "식비")
        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(
            post("/plan/categories")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.name").value("식비"))
    }

    @Test
    fun `list returns categories`() {
        val categories = listOf(
            Category(userId = 1L, type = CategoryType.EXPENSE, name = "식비").apply { id = 1L; onCreate() },
            Category(userId = 1L, type = CategoryType.INCOME, name = "급여").apply { id = 2L; onCreate() },
        )
        every { categoryService.list(1L) } returns categories

        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(get("/plan/categories").principal(principal))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
    }

    @Test
    fun `update returns updated category`() {
        val category = Category(userId = 1L, type = CategoryType.EXPENSE, name = "생활비").apply {
            id = 10L
            onCreate()
        }
        every { categoryService.update(1L, 10L, "생활비") } returns category

        val request = CategoryUpdateRequest(name = "생활비")
        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(
            patch("/plan/categories/10")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.name").value("생활비"))
    }

    @Test
    fun `delete returns no content`() {
        every { categoryService.delete(1L, 10L) } returns Unit

        val principal = TestingAuthenticationToken("1", "credentials")

        mockMvc.perform(delete("/plan/categories/10").principal(principal))
            .andExpect(status().isNoContent)
    }
}
