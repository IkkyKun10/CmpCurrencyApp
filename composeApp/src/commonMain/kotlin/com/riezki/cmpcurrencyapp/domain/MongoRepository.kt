package com.riezki.cmpcurrencyapp.domain

import com.riezki.cmpcurrencyapp.data.remote.dto.Currency
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState
import kotlinx.coroutines.flow.Flow

/**
 * @author riezky maisyar
 */

interface MongoRepository {
    fun configureRealmDb()
    suspend fun insertCurrencyData(entity: Currency)
    fun readCurrencyData() : Flow<RequestState<List<Currency>>>
    suspend fun cleanUp()
}