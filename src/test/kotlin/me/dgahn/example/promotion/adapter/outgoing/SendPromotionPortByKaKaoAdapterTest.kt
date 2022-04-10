package me.dgahn.example.promotion.adapter.outgoing

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import me.dgahn.example.common.adapter.outgoing.ClientConfig
import me.dgahn.example.promotion.domain.MessageStatus
import me.dgahn.example.promotion.domain.MessageType
import me.dgahn.example.promotion.fixture.PromotionFixture.createPromotion
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(
    classes = [
        ClientConfig::class,
        SendPromotionPortByKaKaoAdapter::class
    ]
)
@ExtendWith(SpringExtension::class)
internal class SendPromotionPortByKaKaoAdapterTest(
    @Autowired private val adapter: SendPromotionPortByKaKaoAdapter
) {

    @Test
    fun `카카오 메시지 프로바이더로 메시지를 전달할 수 있다`() = runBlocking {
        val promotion = createPromotion()
        val actual = adapter.send(promotion)
        actual.forEach { result ->
            result.promotionId shouldBe promotion.id
            result.type shouldBe MessageType.KAKAO
            result.status shouldBe MessageStatus.SUCCESS
        }
    }
}
