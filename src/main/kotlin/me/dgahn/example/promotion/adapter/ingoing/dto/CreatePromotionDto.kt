package me.dgahn.example.promotion.adapter.ingoing.dto

import kotlinx.serialization.Serializable
import me.dgahn.example.common.serialization.UUIDSerializer
import me.dgahn.example.promotion.adapter.ingoing.dto.RecipientResponseDto.Companion.toDomain
import me.dgahn.example.promotion.domain.Promotion
import java.util.UUID

@Serializable
data class CreatePromotionRequestDto(
    val message: String,
    val recipients: List<RecipientResponseDto>
) {
    init {
        require(message.isNotBlank()) { "message must not be empty" }
        require(recipients.isNotEmpty()) { "recipients must not be empty" }
    }

    fun toDomain() = Promotion(
        message = message,
        recipients = recipients.toDomain()
    )
}

@Serializable
data class CreatePromotionResponseDto(
    @Serializable(with = UUIDSerializer::class)
    val identifier: UUID,
    val messageCount: Int
) {
    init {
        require(messageCount >= MESSAGE_COUNT_MIN_VALUE) {
            "The message count must be at least $MESSAGE_COUNT_MIN_VALUE"
        }
    }

    companion object {
        private const val MESSAGE_COUNT_MIN_VALUE = 0
        fun Promotion.toCreatePromotionResponseDto() = CreatePromotionResponseDto(
            identifier = id,
            messageCount = recipients.size
        )
    }
}
