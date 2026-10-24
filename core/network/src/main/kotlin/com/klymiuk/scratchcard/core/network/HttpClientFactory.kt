package com.klymiuk.scratchcard.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    private const val TIMEOUT_MILLIS = 15_000L

    fun create(
        engine: HttpClientEngine,
        json: Json,
        logger: Logger? = null,
    ): HttpClient = HttpClient(engine) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT_MILLIS
            connectTimeoutMillis = TIMEOUT_MILLIS
            socketTimeoutMillis = TIMEOUT_MILLIS
        }

        if (logger != null) {
            install(Logging) {
                this.logger = logger
                level = LogLevel.INFO
            }
        }
    }
}

val NetworkJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}
