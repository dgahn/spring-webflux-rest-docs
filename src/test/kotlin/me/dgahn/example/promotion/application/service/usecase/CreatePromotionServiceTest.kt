package me.dgahn.example.promotion.application.service.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.dgahn.example.promotion.application.port.outgoing.SendPromotionPort
import me.dgahn.example.promotion.fixture.PromotionFixture.createPromotion
import org.junit.jupiter.api.Test

internal class CreatePromotionServiceTest {
    private val sendPromotionPortMock = mockk<SendPromotionPort>()
    private val service = CreatePromotionService(sendPromotionPortMock, sendPromotionPortMock)

    @Test
    fun `프로모션 메시지를 전달할 수 있다`() = runBlocking {
        val promotion = createPromotion()
        coEvery { sendPromotionPortMock.send(promotion) } returns emptyList()
        service.create(promotion)
        delay(100L)
        coVerify { sendPromotionPortMock.send(promotion) }
    }
}
