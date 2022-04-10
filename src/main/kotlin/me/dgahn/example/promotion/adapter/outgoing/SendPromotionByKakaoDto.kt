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
data class SendPromotionByKakaoRequestDto(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val content: String,
    val channelId: String,
    val recipient: String
) {
    companion object {
        private const val DEFAULT_CHANNEL_ID = "c32803d8057b40ba9ed5c9c6ad1c1f18"
        private const val CHANGE_NAME_FORMAT = "{name}"
        private const val CHANGE_POINT_FORMAT = "{point}"
        fun from(promotion: Promotion) = promotion.recipients.map { recipient ->
            SendPromotionByKakaoRequestDto(
                uuid = promotion.id,
                content = promotion.message
                    .replace(CHANGE_NAME_FORMAT, recipient.name)
                    .replace(CHANGE_POINT_FORMAT, recipient.point.toString()),
                channelId = DEFAULT_CHANNEL_ID,
                recipient = recipient.phoneNumber.value
            )
        }
    }
}

@Serializable
data class SendPromotionByKakaoResponseDto(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val resultCode: String
) {
    fun toDomain(type: MessageType, sentAt: LocalDateTime, recipient: String) = PromotionResult(
        promotionId = this.uuid,
        type = type,
        status = MessageStatus.SUCCESS,
        sentAt = sentAt,
        recipient = PhoneNumber(recipient)
    )
}
