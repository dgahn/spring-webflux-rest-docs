package me.dgahn.example.promotion.fixture

import me.dgahn.example.promotion.adapter.ingoing.dto.RecipientResponseDto
import me.dgahn.example.promotion.domain.PhoneNumber
import me.dgahn.example.promotion.domain.Recipient

object RecipientFixture {
    fun createRecipientResponseDto(recipientCount: Int): List<RecipientResponseDto> = (1..recipientCount).map {
        RecipientResponseDto(
            phoneNumber = "+82101234${getLastPhoneNumber(it.toString())}",
            name = "test$it",
            point = it
        )
    }

    fun createRecipient(recipientCount: Int) = (1..recipientCount).map {
        Recipient(
            phoneNumber = PhoneNumber("+82101234${getLastPhoneNumber(it.toString())}"),
            name = "test$it",
            point = it
        )
    }

    private fun getLastPhoneNumber(numberString: String): String = when (numberString.length) {
        1 -> "000$numberString"
        2 -> "00$numberString"
        3 -> "0$numberString"
        4 -> numberString
        else -> throw IllegalArgumentException("numberString cannot exceed 4 characters.")
    }
}
