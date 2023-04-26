package com.poisonedyouth.springcoroutines.application.service

import arrow.core.Either
import com.poisonedyouth.coroutines.domain.service.port.UUIDServicePort
import com.poisonedyouth.coroutines.domain.service.port.exception.UUIDGenerationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.UUID

@Service
class UUIDWebService : UUIDServicePort {
    private val logger = LoggerFactory.getLogger(UUIDWebService::class.java)
    private val webClient = WebClient.create("http://localhost:8081/api/v1//uuid")

    override suspend fun getNextUUID(): Either<UUIDGenerationException, UUID> {
        return try {
            val uuid: UUID = select {
                retrieveDataByRest().onReceiveCatching {
                    logger.info("Use uuid of rest...")
                    it.getOrThrow()
                }
                retrieveDataByFile().onReceiveCatching {
                    logger.info("Use uuid of file...")
                    it.getOrThrow()
                }
            }
            Either.Right(uuid)
        } catch (e: IllegalStateException) {
            Either.Left(UUIDGenerationException("Failed to generate UUID", e))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun retrieveDataByFile() = CoroutineScope(SupervisorJob()).produce<UUID> {
        val resource = UUIDWebService::class.java.classLoader
            .getResourceAsStream("UUIDS") ?: error("Resource not found!")
        send(
            UUID
                .fromString(resource.bufferedReader().lines().toList().random())
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun retrieveDataByRest() =
        CoroutineScope(SupervisorJob()).produce<UUID> {
            send(webClient.get().retrieve().awaitBody())
        }

}
