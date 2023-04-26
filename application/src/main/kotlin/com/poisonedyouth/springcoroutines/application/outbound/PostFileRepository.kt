package com.poisonedyouth.springcoroutines.application.outbound

import com.fasterxml.jackson.databind.ObjectMapper
import com.poisonedyouth.coroutines.domain.model.Post
import com.poisonedyouth.coroutines.domain.service.port.PostRepositoryPort
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.springframework.stereotype.Repository
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.UUID
import kotlin.io.path.notExists

@Repository
class PostFileRepository(
    private val objectMapper: ObjectMapper
) : PostRepositoryPort {
    private val basePath = Paths.get("data")
    private val storeFile = basePath.resolve("data.txt")

    init {
        if (basePath.notExists()) {
            Files.createDirectories(basePath)
        }
        if(storeFile.notExists()){
            Files.createFile(storeFile)
        }
    }

    override suspend fun findById(id: UUID): Post? = coroutineScope {
        async {
            Files.readAllLines(storeFile).map { ObjectMapper().readValue(it, Post::class.java) }
                .firstOrNull { it.id == id }
        }.await()
    }

    override suspend fun findAll(): Flow<Post> = coroutineScope {
        async {
            Files.readAllLines(storeFile).map {objectMapper.readValue(it, Post::class.java) }
                .asFlow()
        }.await()
    }

    override suspend fun save(post: Post): UUID = coroutineScope {
        async {
            Files.writeString(storeFile, objectMapper.writeValueAsString(post) + "\n", StandardOpenOption.APPEND)
            post.id
        }.await()
    }
}