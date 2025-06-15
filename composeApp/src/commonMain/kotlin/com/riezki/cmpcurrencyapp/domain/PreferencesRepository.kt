package com.riezki.cmpcurrencyapp.domain

import com.riezki.cmpcurrencyapp.domain.data.model.CurrencyCode
import kotlinx.coroutines.flow.Flow

/**
 * @author riezky maisyar
 */

interface PreferencesRepository {
    suspend fun saveLastUpdated(lastUpdated: String)
    suspend fun isDataFresh(currentTimeStamp: Long) : Boolean
    suspend fun saveSourceCurrencyCode(code: String)
    suspend fun saveTargetCurrencyCode(code: String)
    fun readSourceCurrencyCode(): Flow<CurrencyCode>
    fun readTargetCurrencyCode(): Flow<CurrencyCode>
}