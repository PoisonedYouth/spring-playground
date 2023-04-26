package com.poisonedyouth.coroutines.domain.service.port

import arrow.core.Either
import com.poisonedyouth.coroutines.domain.service.port.exception.UUIDGenerationException
import java.util.UUID

interface UUIDServicePort {
    suspend fun getNextUUID(): Either<UUIDGenerationException, UUID>
}