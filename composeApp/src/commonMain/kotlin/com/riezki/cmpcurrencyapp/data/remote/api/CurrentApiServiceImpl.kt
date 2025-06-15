package com.riezki.cmpcurrencyapp.data.remote.api

import com.riezki.cmpcurrencyapp.data.remote.dto.ApiResponse
import com.riezki.cmpcurrencyapp.data.remote.dto.Currency
import com.riezki.cmpcurrencyapp.domain.CurrencyApiService
import com.riezki.cmpcurrencyapp.domain.PreferencesRepository
import com.riezki.cmpcurrencyapp.domain.data.model.CurrencyCode
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * @author riezky maisyar
 */

class CurrentApiServiceImpl(
    private val preferences: PreferencesRepository
) : CurrencyApiService {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15_000L
//            connectTimeoutMillis = 15000L
//            socketTimeoutMillis = 15000L
        }

        install(DefaultRequest) {
            header("apiKey", API_KEY)
            url {
                protocol = io.ktor.http.URLProtocol.HTTPS
                host = HOST_URL
            }
        }
    }

    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val response = httpClient.get(ENDPOINT_LATEST)
            if (response.status.value in 200..299) {
                val dataResponse = response.body<ApiResponse>()
                println("API Response: ${response.bodyAsText()}")

                val availableCurrencyCodes = dataResponse.data.keys
                    .filter {
                        CurrencyCode.entries
                            .map { code -> code.name }
                            .toSet()
                            .contains(it)
                    }

                val availableCurrencies = dataResponse.data.values
                    .filter { currency ->
                        availableCurrencyCodes.contains(currency.code)
                    }

                val currentTimeStamp = dataResponse.meta.lastUpdatedAt
                preferences.saveLastUpdated(currentTimeStamp)

                RequestState.Success(data = availableCurrencies)
            } else {
                println("API Response: ${response.bodyAsText()}")
                RequestState.Error(message = "Something went wrong -> status code: ${response.status}")
            }
        } catch (e: Exception) {
            RequestState.Error(e.message.toString())
        }
    }

    companion object {
        const val HOST_URL = "api.currencyapi.com/v3"
        const val ENDPOINT_LATEST = "latest"
        const val API_KEY = "cur_live_YpO7NTM9L9HNmz12AnMtbnfFFZ1bTB0V5YdwzTFi"
    }
}