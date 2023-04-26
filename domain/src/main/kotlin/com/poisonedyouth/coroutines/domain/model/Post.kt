package com.poisonedyouth.coroutines.domain.model

import java.time.OffsetDateTime
import java.util.UUID

data class Post(
    val id: UUID,
    val title: String,
    val content: String,
    val author: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)