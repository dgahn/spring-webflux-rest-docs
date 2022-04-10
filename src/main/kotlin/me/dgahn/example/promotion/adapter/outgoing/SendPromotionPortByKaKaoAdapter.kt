package me.dgahn.example.promotion.adapter.outgoing

import me.dgahn.example.promotion.application.port.outgoing.SendPromotionPort
import me.dgahn.example.promotion.domain.Promotion
import me.dgahn.example.promotion.domain.PromotionResult
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
@Primary
@Qualifier(value = "sendPromotionPortByKaKaoAdapter")
class SendPromotionPortByKaKaoAdapter(
    private val clientBuilder: WebClient.Builder,
) : SendPromotionPort {
    private val client = clientBuilder.build()

    override suspend fun send(promotion: Promotion): List<PromotionResult> {
        TODO("Not yet implemented")
    }

    override suspend fun send(promotion: Promotion, failResults: List<PromotionResult>): List<PromotionResult> {
        TODO("Not yet implemented")
    }

    companion object {
        val logger = KotlinLogging.logger { }
    }
}
