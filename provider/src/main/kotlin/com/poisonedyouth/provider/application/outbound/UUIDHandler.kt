package com.poisonedyouth.provider.application.outbound

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.util.UUID


@Component
class UUIDHandler {

    suspend fun uuid(req: ServerRequest) = ok().bodyValueAndAwait(UUID.randomUUID())
}