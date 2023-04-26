package com.poisonedyouth.coroutines.domain.service.port.exception

class UUIDGenerationException(
    override val message: String,
    override val cause: Throwable
) : RuntimeException(message, cause)