package me.dgahn.example.promotion.adapter.ingoing.web

import me.dgahn.example.promotion.adapter.ingoing.dto.CreatePromotionRequestDto
import me.dgahn.example.promotion.adapter.ingoing.dto.CreatePromotionResponseDto
import me.dgahn.example.promotion.adapter.ingoing.dto.CreatePromotionResponseDto.Companion.toCreatePromotionResponseDto
import me.dgahn.example.promotion.application.port.ingoing.usecase.CreatePromotionUseCase
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/promotions"])
class PromotionController(
    private val createPromotionUseCase: CreatePromotionUseCase
) {

    @PostMapping
    suspend fun create(@RequestBody request: CreatePromotionRequestDto): CreatePromotionResponseDto =
        createPromotionUseCase.create(request.toDomain()).toCreatePromotionResponseDto()

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
