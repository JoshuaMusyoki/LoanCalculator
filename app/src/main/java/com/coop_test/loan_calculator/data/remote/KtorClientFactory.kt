package com.coop_test.loan_calculator.data.remote

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClientFactory {
    fun create(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true // Throws exception for non-2xx codes automatically

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 15000
            }

            defaultRequest {
                url("https://api.coopbank.co.ke/") // Base URL
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }
    }
}