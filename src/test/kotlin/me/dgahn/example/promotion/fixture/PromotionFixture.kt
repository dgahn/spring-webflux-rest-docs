package me.dgahn.example.promotion.fixture

import me.dgahn.example.promotion.domain.Promotion

object PromotionFixture {
    fun createPromotion() = Promotion(
        message = "안녕하세요. {name}고객님! 고객님의 보유 포인트는 {point}P 입니다.",
        recipients = RecipientFixture.createRecipient(3)
    )
}
