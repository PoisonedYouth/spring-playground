package com.poisonedyouth.provider.application.configuration

import com.poisonedyouth.provider.application.outbound.UUIDHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routing {

    @Bean
    fun routes(postUUIDHandler: UUIDHandler): RouterFunction<ServerResponse> {
        return coRouter {
            "api/v1/".nest {
                GET("uuid", postUUIDHandler::uuid)
            }

        }
    }
}