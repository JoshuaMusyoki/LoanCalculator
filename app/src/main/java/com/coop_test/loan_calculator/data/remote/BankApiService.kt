package com.coop_test.loan_calculator.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/**
 * Enterprise-grade Api Service utilizing Ktor Client.
 * Constructor injection allows Koin to seamlessly manage the underlying HttpClient lifecycle.
 */
class BankingApiService(private val client: HttpClient) {

   /** For Remote Datasource Support */
}