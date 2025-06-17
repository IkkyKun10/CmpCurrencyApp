package com.riezki.cmpcurrencyapp.presenter.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.riezki.cmpcurrencyapp.data.remote.dto.Currency
import com.riezki.cmpcurrencyapp.domain.CurrencyApiService
import com.riezki.cmpcurrencyapp.domain.MongoRepository
import com.riezki.cmpcurrencyapp.domain.PreferencesRepository
import com.riezki.cmpcurrencyapp.domain.data.model.RateStatus
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState
import com.riezki.cmpcurrencyapp.presenter.event.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

/**
 * @author riezky maisyar
 */

class HomeViewModel(
    private val preference: PreferencesRepository,
    private val currencyApiService: CurrencyApiService,
    private val mongoRepository: MongoRepository
) : ScreenModel {
    private var _rateStatus = mutableStateOf(RateStatus.Idle)
    val rateStatus: State<RateStatus> = _rateStatus

    private var _allCurrencies = mutableStateListOf<Currency>()
    val allCurrencies: List<Currency> = _allCurrencies

    private var _sourceCurrency: MutableState<RequestState<Currency>> =
        mutableStateOf(RequestState.Idle)
    val sourceCurrency: State<RequestState<Currency>> = _sourceCurrency

    private var _targetCurrency: MutableState<RequestState<Currency>> =
        mutableStateOf(RequestState.Idle)
    val targetCurrency: State<RequestState<Currency>> = _targetCurrency

    fun sendEvent(event: UiEvent) {
        when (event) {
            is UiEvent.RefreshRate -> {
                screenModelScope.launch {
                    fetchNewRates()
                }
            }
            UiEvent.SwitchCurrencies -> switchCurrency()

            is UiEvent.SaveSourceCurrencyCode -> saveSourceCurrencyCode(event.code)
            is UiEvent.SaveTargetCurrencyCode -> saveTargetCurrencyCode(event.code)
        }
    }

    private fun saveSourceCurrencyCode(code: String) {
        screenModelScope.launch(Dispatchers.IO) {
            preference.saveSourceCurrencyCode(code)
        }
    }

    private fun saveTargetCurrencyCode(code: String) {
        screenModelScope.launch(Dispatchers.IO) {
            preference.saveTargetCurrencyCode(code)
        }
    }

    private suspend fun getRates() {
        _rateStatus.value = if (preference.isDataFresh(
            currentTimeStamp = Clock.System.now().toEpochMilliseconds()
        )) {
            RateStatus.Fresh
        } else RateStatus.Stale
    }

    private suspend fun fetchNewRates() {
        try {
            val localCache = mongoRepository.readCurrencyData().first()
            if (localCache.isSuccess()) {
                if (localCache.getSuccessData().isNotEmpty()) {
                    println("HomeViewModel: DATABASE IS FULL")
                    _allCurrencies.clear()
                    _allCurrencies.addAll(localCache.getSuccessData())
                    if (!preference.isDataFresh(Clock.System.now().toEpochMilliseconds())) {
                        println("HomeViewModel: FETCHING NEW RATES")
                        cacheTheData()
                    } else {
                        println("HomeViewModel: DATA IS FRESH")
                    }
                } else {
                    println("HomeViewModel: DATABASE NEEDS DATA")
                    cacheTheData()
                }
            } else if (localCache.isError()) {
                println("HomeViewModel: ERROR READING LOCAL DATABASE ${localCache.getErrorMsg()}")
            }
            getRates()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun cacheTheData() {
        val fetchedData = currencyApiService.getLatestExchangeRates()
        if (fetchedData.isSuccess()) {
            mongoRepository.cleanUp()
            fetchedData.getSuccessData().forEach {
                println("HomeViewModel: ADDING ${it.code}")
                mongoRepository.insertCurrencyData(it)
            }
            println("HomeViewModel: UPDATING _allCurrencies")
            _allCurrencies.clear()
            _allCurrencies.addAll(fetchedData.getSuccessData())
        } else if (fetchedData.isError()) {
            println("HomeViewModel: FETCHING FAILED ${fetchedData.getErrorMsg()}")
        }
    }

    private fun readSourceCurrency() {
        screenModelScope.launch {
            preference.readSourceCurrencyCode().collectLatest { currencyCode ->
                val selectedCurrency = _allCurrencies.find { it.code == currencyCode.name }
                if (selectedCurrency != null) {
                    _sourceCurrency.value = RequestState.Success(data = selectedCurrency)
                } else {
                    _sourceCurrency.value = RequestState.Error(message = "Currency not found")
                }
            }
        }
    }

    private fun readTargetCurrency() {
        screenModelScope.launch {
            preference.readTargetCurrencyCode().collectLatest { currencyCode ->
                val selectedCurrency = _allCurrencies.find { it.code == currencyCode.name }
                if (selectedCurrency != null) {
                    _targetCurrency.value = RequestState.Success(data = selectedCurrency)
                } else {
                    _targetCurrency.value = RequestState.Error(message = "Currency not found")
                }
            }
        }
    }

    private fun switchCurrency() {
        val source = sourceCurrency.value
        val target = targetCurrency.value
        _sourceCurrency.value = target
        _targetCurrency.value = source
    }

    init {
        screenModelScope.launch {
            fetchNewRates()
            readSourceCurrency()
            readTargetCurrency()
        }
    }
}