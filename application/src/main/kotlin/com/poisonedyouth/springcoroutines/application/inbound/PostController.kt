package com.poisonedyouth.springcoroutines.application.inbound

import com.poisonedyouth.coroutines.domain.model.Post
import com.poisonedyouth.coroutines.domain.service.port.PostServicePort
import com.poisonedyouth.coroutines.domain.service.port.UUIDServicePort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.OffsetDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostServicePort,
    private val uuidService: UUIDServicePort,
) {

    @GetMapping
    suspend fun getAllPosts() = postService.getAllPosts()

    @GetMapping("/{id}")
    suspend fun getPostById(@PathVariable id: UUID) = postService.getPost(id)

    @PostMapping
    suspend fun createPost(@RequestBody post: PostDto): ResponseEntity<Any> {
        return uuidService.getNextUUID().fold(
            ifLeft = {
                ResponseEntity.badRequest().body("Failed to generate post")
            },
            ifRight = {
                ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(post.toPost(it)))
            })
    }

    private suspend fun PostDto.toPost(uuid: UUID): Post {
        val new = OffsetDateTime.now()
        return Post(
            id = uuid,
            title = title,
            content = content,
            author = author,
            createdAt = new,
            updatedAt = new
        )
    }
}