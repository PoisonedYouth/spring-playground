package com.poisonedyouth.springcoroutines.application.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SerializationConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            this.registerModule(kotlinModule())
            this.registerModule(JavaTimeModule())
            this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }
}