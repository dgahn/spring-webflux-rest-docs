package me.dgahn.example.promotion.domain

import java.time.LocalDateTime
import java.util.UUID

data class PromotionResult(
    val promotionId: UUID,
    val status: MessageStatus,
    val type: MessageType,
    val sentAt: LocalDateTime,
    val recipient: PhoneNumber,
    val failedAt: LocalDateTime? = null
) {
    companion object {
        fun createFailResult(
            uuid: UUID,
            type: MessageType,
            sentAt: LocalDateTime,
            recipient: String
        ): PromotionResult = PromotionResult(
            promotionId = uuid,
            status = MessageStatus.FAIL,
            type = type,
            sentAt = sentAt,
            recipient = PhoneNumber(recipient),
            failedAt = LocalDateTime.now()
        )
    }

    fun isFailed() = this.status == MessageStatus.FAIL
}

enum class MessageType {
    KAKAO,
    LMS
}

enum class MessageStatus {
    SUCCESS,
    FAIL
}
