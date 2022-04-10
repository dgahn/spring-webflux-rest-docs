package me.dgahn.example.common.adapter.outgoing

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class ClientConfig {

    @Bean
    fun clientConnector(): ClientHttpConnector = ReactorClientHttpConnector(
        HttpClient.create().secure { sslContextSpec ->
            sslContextSpec.sslContext(
                SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build()
            )
        }
            .doOnConnected { conn ->
                conn.addHandlerLast(
                    ReadTimeoutHandler(
                        READ_WRITE_TIMEOUT,
                        TimeUnit.MILLISECONDS
                    )
                )
                conn.addHandlerLast(
                    WriteTimeoutHandler(
                        READ_WRITE_TIMEOUT,
                        TimeUnit.MILLISECONDS
                    )
                )
            }
    )

    @Bean
    @Scope("prototype")
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()
        .clientConnector(clientConnector())
        .defaultHeader("Authorization", "Bearer K8vlM_6vyhxuv_X98jlWzj4QimU0R6rt")

    companion object {
        private const val READ_WRITE_TIMEOUT = 3_000L
    }
}
