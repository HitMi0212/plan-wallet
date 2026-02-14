package com.planwallet.global.error

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.Locale

/**
 * 전역 예외 처리 핸들러.
 */
@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource,
) {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
        locale: Locale,
    ): ResponseEntity<ApiErrorResponse> {
        val fieldError = ex.bindingResult.fieldErrors.firstOrNull()
        val code = "error.validation"
        val message = fieldError?.let { resolveFieldErrorMessage(it, locale) }
            ?: messageSource.getMessage(code, null, locale)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse(code, message, HttpStatus.BAD_REQUEST, request))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
        ex: ConstraintViolationException,
        request: HttpServletRequest,
        locale: Locale,
    ): ResponseEntity<ApiErrorResponse> {
        val code = "error.validation"
        val message = ex.constraintViolations.firstOrNull()?.message
            ?: messageSource.getMessage(code, null, locale)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse(code, message, HttpStatus.BAD_REQUEST, request))
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(
        ex: ResponseStatusException,
        request: HttpServletRequest,
        locale: Locale,
    ): ResponseEntity<ApiErrorResponse> {
        val status = ex.statusCode
        val code = statusToCode(status.value())
        val message = ex.reason
            ?: messageSource.getMessage(code, null, locale)

        val httpStatus = HttpStatus.valueOf(status.value())
        return ResponseEntity
            .status(httpStatus)
            .body(errorResponse(code, message, httpStatus, request))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnhandled(
        ex: Exception,
        request: HttpServletRequest,
        locale: Locale,
    ): ResponseEntity<ApiErrorResponse> {
        val code = "error.internal"
        val message = messageSource.getMessage(code, null, locale)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse(code, message, HttpStatus.INTERNAL_SERVER_ERROR, request))
    }

    private fun statusToCode(status: Int): String = when (status) {
        400 -> "error.bad_request"
        401 -> "error.unauthorized"
        403 -> "error.forbidden"
        404 -> "error.not_found"
        409 -> "error.conflict"
        else -> "error.internal"
    }

    private fun resolveFieldErrorMessage(fieldError: FieldError, locale: Locale): String {
        return messageSource.getMessage(fieldError, locale)
    }

    private fun errorResponse(
        code: String,
        message: String,
        status: HttpStatus,
        request: HttpServletRequest,
    ): ApiErrorResponse {
        return ApiErrorResponse(
            code = code,
            message = message,
            status = status.value(),
            path = request.requestURI,
            timestamp = Instant.now(),
        )
    }
}
