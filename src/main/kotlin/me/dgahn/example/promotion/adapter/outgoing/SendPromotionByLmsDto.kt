package me.dgahn.example.promotion.adapter.outgoing

import kotlinx.serialization.Serializable
import me.dgahn.example.common.serialization.UUIDSerializer
import me.dgahn.example.promotion.domain.MessageStatus
import me.dgahn.example.promotion.domain.MessageType
import me.dgahn.example.promotion.domain.PhoneNumber
import me.dgahn.example.promotion.domain.Promotion
import me.dgahn.example.promotion.domain.PromotionResult
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class SendPromotionByLmsRequestDto(
    val title: String,
    val text: String,
    val from: String,
    val destinations: List<String>
) {
    companion object {
        private const val DEFAULT_FROM = "+8225446463"
        fun create(promotion: Promotion, failResults: List<PromotionResult>): SendPromotionByLmsRequestDto =
            SendPromotionByLmsRequestDto(
                title = promotion.message,
                text = promotion.message,
                from = DEFAULT_FROM,
                destinations = failResults.map { it.recipient.value }
            )
    }
}

@Serializable
data class SendPromotionByLmsResponseDto(
    @Serializable(with = UUIDSerializer::class)
    val groupId: UUID,
    val toCount: String,
    val destinations: List<SendPromotionByLmsDestinationResponseDto>
) {
    fun toDomain(sentAt: LocalDateTime, promotionId: UUID): List<PromotionResult> = destinations.map {
        PromotionResult(
            promotionId = promotionId,
            status = MessageStatus.SUCCESS,
            type = MessageType.LMS,
            sentAt = sentAt,
            recipient = PhoneNumber(it.to)
        )
    }
}

@Serializable
data class SendPromotionByLmsDestinationResponseDto(
    @Serializable(with = UUIDSerializer::class)
    val messageId: UUID,
    val to: String,
    val status: String
)
