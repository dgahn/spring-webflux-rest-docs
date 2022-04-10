package me.dgahn.example.promotion.adapter.outgoing

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

val logger = KotlinLogging.logger { }

suspend inline fun <reified T> WebClient.post(uri: String, requestBodyJson: String): T = this.post()
    .uri(uri)
    .contentType(MediaType.APPLICATION_JSON)
    .bodyValue(requestBodyJson)
    .awaitExchange { response ->
        val responseString = response.awaitBody<String>()
        logger.debug { "request $requestBodyJson" }
        Json.decodeFromString(responseString)
    }
