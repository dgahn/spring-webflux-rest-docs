package me.dgahn.example.promotion.adapter.ingoing.dto

import kotlinx.serialization.Serializable
import me.dgahn.example.promotion.domain.PhoneNumber
import me.dgahn.example.promotion.domain.Recipient

@Serializable
data class RecipientResponseDto(
    val phoneNumber: String,
    val name: String,
    val point: Int
) {
    init {
        require(point >= POINT_MIN_VALUE) { "The point must be at least $POINT_MIN_VALUE" }
    }

    fun toDomain(): Recipient = Recipient(
        phoneNumber = PhoneNumber(phoneNumber),
        name = name,
        point = point
    )

    companion object {
        private const val POINT_MIN_VALUE = 0
        fun List<RecipientResponseDto>.toDomain(): List<Recipient> = this.map { it.toDomain() }
    }
}
