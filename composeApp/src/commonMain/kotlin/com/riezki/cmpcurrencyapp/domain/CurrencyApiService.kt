package com.riezki.cmpcurrencyapp.domain

import com.riezki.cmpcurrencyapp.data.remote.dto.Currency
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState

/**
 * @author riezky maisyar
 */

interface CurrencyApiService {
    suspend fun getLatestExchangeRates() : RequestState<List<Currency>>
}