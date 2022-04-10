package me.dgahn.example.promotion.application.port.outgoing

import me.dgahn.example.promotion.domain.Promotion
import me.dgahn.example.promotion.domain.PromotionResult

interface SendPromotionPort {
    suspend fun send(promotion: Promotion): List<PromotionResult>

    suspend fun send(promotion: Promotion, failResults: List<PromotionResult>): List<PromotionResult>
}
