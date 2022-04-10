package me.dgahn.example.promotion.adapter.ingoing.web

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.dgahn.example.promotion.adapter.ingoing.dto.CreatePromotionRequestDto
import me.dgahn.example.promotion.adapter.ingoing.dto.CreatePromotionResponseDto
import me.dgahn.example.promotion.adapter.ingoing.dto.RecipientResponseDto.Companion.toDomain
import me.dgahn.example.promotion.application.port.ingoing.usecase.CreatePromotionUseCase
import me.dgahn.example.promotion.domain.Promotion
import me.dgahn.example.promotion.fixture.RecipientFixture.createRecipientResponseDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.util.UUID

@WebFluxTest
@ExtendWith(value = [RestDocumentationExtension::class])
internal class PromotionControllerUnitTest(
    @Autowired private val context: ApplicationContext
) {
    @MockkBean
    lateinit var createPromotionUseCase: CreatePromotionUseCase
    lateinit var client: WebTestClient

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        client = WebTestClient.bindToApplicationContext(this.context).configureClient()
            .filter(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    fun `프로모션 생성시 recipients가 존재하지 않으면 BadRequest를 받는다`() {
        val requestBody = createEmptyRecipientRequestJson()
        coEvery { createPromotionUseCase.create(any()) } returns Promotion(message = "", recipients = emptyList())
        client.post().uri("/promotions")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .consumeWith(
                document(
                    "create-promotion-bad-request-by-empty-recipients",
                    requestFields(
                        fieldWithPath("message").description("마켓팅 문자에서 활용할 메시지"),
                        fieldWithPath("recipients").description("고객 목록"),
                    ),
                    responseFields(
                        fieldWithPath("timestamp").description("에러가 발생한 시간"),
                        fieldWithPath("message").description("에러에 대한 메시지"),
                        fieldWithPath("code").description("에러에 대한 내부 에러 코드"),
                        fieldWithPath("status").description("에러에 대한 HTTP 상태 코드")
                    )
                )
            )
    }

    private fun createEmptyRecipientRequestJson() = "{\"message\":\"message\",\"recipients\":[]}"

    @Test
    fun `프로모션 생성시 message가 존재하지 않으면 BadRequest를 받는다`() {
        val requestBody = createEmptyMessageRequestJson()
        coEvery { createPromotionUseCase.create(any()) } returns Promotion(message = "", recipients = emptyList())
        client.post().uri("/promotions")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .consumeWith(
                document(
                    "create-promotion-bad-request-by-empty-message",
                    requestFields(
                        fieldWithPath("message").description("마켓팅 문자에서 활용할 메시지"),
                        fieldWithPath("recipients").description("고객 목록"),
                        fieldWithPath("recipients[].phoneNumber").description("고객의 전화번호"),
                        fieldWithPath("recipients[].name").description("고객의 이름"),
                        fieldWithPath("recipients[].point").description("고객이 가진 포인트"),
                    ),
                    responseFields(
                        fieldWithPath("timestamp").description("에러가 발생한 시간"),
                        fieldWithPath("message").description("에러에 대한 메시지"),
                        fieldWithPath("code").description("에러에 대한 내부 에러 코드"),
                        fieldWithPath("status").description("에러에 대한 HTTP 상태 코드")
                    )
                )
            )
    }

    private fun createEmptyMessageRequestJson() =
        "{\"message\":\"\",\"recipients\":[{\"phoneNumber\":\"010-0000-0001\",\"name\":\"a\",\"point\":1}]}"

    @ParameterizedTest
    @ValueSource(ints = [1, 3, 10])
    fun `프로모션을 생성할 때 다수의 recipient 목록을 가질 수 있다`(recipientCount: Int) {
        val requestDto = createPromotionRequestDto(recipientCount)
        val id = UUID.randomUUID()
        coEvery { createPromotionUseCase.create(requestDto.toDomain()) } returns createPromotion(requestDto, id)
        val requestBody = Json.encodeToString(requestDto)
        val expected = Json.encodeToString(createPromotionResponseDto(requestDto, id))

        client.post().uri("/promotions")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody<String>()
            .consumeWith {
                it.responseBody shouldBe expected
            }
    }

    @Test
    fun `프로모션을 생성할 수 있다`() {
        val requestDto = createPromotionRequestDto()
        val id = UUID.randomUUID()
        coEvery { createPromotionUseCase.create(requestDto.toDomain()) } returns createPromotion(requestDto, id)
        val requestBody = Json.encodeToString(requestDto)

        client.post().uri("/promotions")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .consumeWith(
                document(
                    "create-promotion-ok",
                    requestFields(
                        fieldWithPath("message").description("마켓팅 문자에서 활용할 메시지"),
                        fieldWithPath("recipients").description("고객 목록"),
                        fieldWithPath("recipients[].phoneNumber").description("고객의 전화번호"),
                        fieldWithPath("recipients[].name").description("고객의 이름"),
                        fieldWithPath("recipients[].point").description("고객이 가진 포인트"),
                    ),
                    responseFields(
                        fieldWithPath("identifier").description("프로모션 모델의 식별자 값"),
                        fieldWithPath("messageCount").description("보낸 메시지의 수")
                    )
                )
            )
    }

    private fun createPromotion(requestDto: CreatePromotionRequestDto, id: UUID): Promotion = Promotion(
        message = requestDto.message,
        recipients = requestDto.recipients.toDomain(),
        id = id
    )

    private fun createPromotionResponseDto(
        requestDto: CreatePromotionRequestDto,
        id: UUID
    ): CreatePromotionResponseDto =
        CreatePromotionResponseDto(
            identifier = id,
            messageCount = requestDto.recipients.size
        )

    private fun createPromotionRequestDto(recipientCount: Int = 1): CreatePromotionRequestDto =
        CreatePromotionRequestDto(
            message = "안녕하세요. {name}고객님! 고객님의 보유 포인트는 {point}P 입니다.",
            recipients = createRecipientResponseDto(recipientCount)
        )
}
