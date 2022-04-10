package me.dgahn.example.promotion.application.service.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.dgahn.example.promotion.application.port.ingoing.usecase.CreatePromotionUseCase
import me.dgahn.example.promotion.application.port.outgoing.SendPromotionPort
import me.dgahn.example.promotion.domain.Promotion
import me.dgahn.example.promotion.domain.PromotionResult
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class CreatePromotionService(
    @Qualifier("sendPromotionPortByKaKaoAdapter")
    private val sendPromotionPortByKakaoAdapter: SendPromotionPort,
    @Qualifier("sendPromotionPortByLmsAdapter")
    private val sendPromotionPortByLmsAdapter: SendPromotionPort
) : CreatePromotionUseCase {
    override suspend fun create(promotion: Promotion): Promotion {
        CoroutineScope(Dispatchers.Default).launch {
            val failResults: List<PromotionResult> = sendPromotionPortByKakaoAdapter.send(promotion)
                .apply { logger.debug { "result $this" } }
                .filter { it.isFailed() }
            if (failResults.isNotEmpty()) {
                sendPromotionPortByLmsAdapter.send(promotion, failResults)
            }
        }
        return promotion
    }
    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
