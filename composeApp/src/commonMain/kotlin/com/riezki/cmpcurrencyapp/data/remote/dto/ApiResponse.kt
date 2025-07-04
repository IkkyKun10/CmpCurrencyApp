package com.riezki.cmpcurrencyapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author riezky maisyar
 */

@Serializable
data class ApiResponse(
    val meta: MetaData,
    val data: Map<String, Currency>
)

@Serializable
data class MetaData(
    @SerialName("last_updated_at")
    val lastUpdatedAt: String
)

@Serializable
data class Currency(
    val code: String,
    val value: Double
)