package com.planwallet.presentation.transaction

import com.planwallet.application.transaction.TransactionService
import com.planwallet.presentation.transaction.dto.TransactionCreateRequest
import com.planwallet.presentation.transaction.dto.TransactionResponse
import com.planwallet.presentation.transaction.dto.TransactionUpdateRequest
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
 * 거래 API 컨트롤러.
 */
@Tag(name = "Transaction", description = "거래 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/plan/transactions")
class TransactionController(
    private val transactionService: TransactionService,
) {
    /**
     * 거래 생성.
     */
    @Operation(summary = "거래 생성", description = "거래를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        authentication: Authentication,
        @Valid @RequestBody request: TransactionCreateRequest,
    ): TransactionResponse {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        val transaction = transactionService.create(
            userId = userId,
            type = request.type,
            amount = request.amount,
            categoryId = request.categoryId,
            memo = request.memo,
            occurredAt = request.occurredAt,
        )
        return TransactionResponse(
            id = transaction.id ?: 0,
            type = transaction.type,
            amount = transaction.amount,
            categoryId = transaction.categoryId,
            memo = transaction.memo,
            occurredAt = transaction.occurredAt,
            createdAt = transaction.createdAt ?: throw IllegalStateException("createdAt not set"),
            updatedAt = transaction.updatedAt ?: throw IllegalStateException("updatedAt not set"),
        )
    }

    /**
     * 거래 목록 조회.
     */
    @Operation(summary = "거래 목록", description = "거래 목록을 조회합니다.")
    @GetMapping
    fun list(authentication: Authentication): List<TransactionResponse> {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        return transactionService.list(userId).map { transaction ->
            TransactionResponse(
                id = transaction.id ?: 0,
                type = transaction.type,
                amount = transaction.amount,
                categoryId = transaction.categoryId,
                memo = transaction.memo,
                occurredAt = transaction.occurredAt,
                createdAt = transaction.createdAt ?: throw IllegalStateException("createdAt not set"),
                updatedAt = transaction.updatedAt ?: throw IllegalStateException("updatedAt not set"),
            )
        }
    }

    /**
     * 거래 수정.
     */
    @Operation(summary = "거래 수정", description = "거래를 수정합니다.")
    @PatchMapping("/{id}")
    fun update(
        authentication: Authentication,
        @PathVariable("id") id: Long,
        @Valid @RequestBody request: TransactionUpdateRequest,
    ): TransactionResponse {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        val transaction = transactionService.update(
            userId = userId,
            transactionId = id,
            type = request.type,
            amount = request.amount,
            categoryId = request.categoryId,
            memo = request.memo,
            occurredAt = request.occurredAt,
        )
        return TransactionResponse(
            id = transaction.id ?: 0,
            type = transaction.type,
            amount = transaction.amount,
            categoryId = transaction.categoryId,
            memo = transaction.memo,
            occurredAt = transaction.occurredAt,
            createdAt = transaction.createdAt ?: throw IllegalStateException("createdAt not set"),
            updatedAt = transaction.updatedAt ?: throw IllegalStateException("updatedAt not set"),
        )
    }

    /**
     * 거래 삭제.
     */
    @Operation(summary = "거래 삭제", description = "거래를 삭제합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(authentication: Authentication, @PathVariable("id") id: Long) {
        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalStateException("Invalid authentication subject")
        transactionService.delete(userId, id)
    }
}
