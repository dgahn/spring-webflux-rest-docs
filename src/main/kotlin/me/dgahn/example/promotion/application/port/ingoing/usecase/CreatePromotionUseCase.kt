package me.dgahn.example.promotion.application.port.ingoing.usecase

import me.dgahn.example.promotion.domain.Promotion

interface CreatePromotionUseCase {
    suspend fun create(promotion: Promotion): Promotion
}
