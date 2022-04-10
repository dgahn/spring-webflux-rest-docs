package me.dgahn.example.promotion.adapter.outgoing

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import me.dgahn.example.common.adapter.outgoing.ClientConfig
import me.dgahn.example.promotion.domain.MessageStatus
import me.dgahn.example.promotion.domain.MessageType
import me.dgahn.example.promotion.domain.PromotionResult
import me.dgahn.example.promotion.fixture.PromotionFixture
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@SpringBootTest(
    classes = [
        ClientConfig::class,
        SendPromotionPortByLmsAdapter::class
    ]
)
@ExtendWith(SpringExtension::class)
internal class SendPromotionPortByLmsAdapterTest(
    @Autowired private val adapter: SendPromotionPortByLmsAdapter
) {

    @Test
    @Disabled
    fun `LMS 메시지 프로바이더로 메시지를 전달할 수 있다`() = runBlocking {
        val promotion = PromotionFixture.createPromotion()
        val failResults = promotion.recipients.map {
            PromotionResult.createFailResult(promotion.id, MessageType.KAKAO, LocalDateTime.now(), it.phoneNumber.value)
        }
        val actual = adapter.send(promotion, failResults)
        actual.forEach { result ->
            result.promotionId shouldBe promotion.id
            result.type shouldBe MessageType.LMS
            result.status shouldBe MessageStatus.SUCCESS
        }
    }
}
