package me.dgahn.example.common.adapter.ingoing.web.handler

import me.dgahn.example.common.adapter.ingoing.web.error.ErrorResponse
import me.dgahn.example.common.adapter.ingoing.web.error.ErrorResponse.Companion.toErrorResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.error { "IllegalArgumentException, ${e.stackTraceToString()}" }
        return ResponseEntity(e.toErrorResponse(), HttpStatus.BAD_REQUEST)
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
