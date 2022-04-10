package me.dgahn.example.common.adapter.ingoing.web.error

import java.time.LocalDateTime

data class ErrorResponse private constructor(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val message: String,
    val code: String,
    val status: Int,
) {
    companion object {
        fun create(
            message: String,
            code: String,
            status: Int,
        ) = ErrorResponse(
            message = message,
            code = code,
            status = status,
        )

        fun IllegalArgumentException.toErrorResponse(): ErrorResponse {
            val errorCode = ErrorCode.INVALID_PARAMETER
            return create(
                message = this.message ?: "",
                code = errorCode.code,
                status = errorCode.status,
            )
        }
    }
}
