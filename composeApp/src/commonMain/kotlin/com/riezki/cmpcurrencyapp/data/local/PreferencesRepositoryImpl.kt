package com.riezki.cmpcurrencyapp.data.local

import com.riezki.cmpcurrencyapp.domain.PreferencesRepository
import com.riezki.cmpcurrencyapp.domain.data.model.CurrencyCode
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * @author riezky maisyar
 */

@OptIn(ExperimentalSettingsApi::class)
class PreferencesRepositoryImpl(
    private val settings: Settings
) : PreferencesRepository {

    companion object {
        const val TIMESTAMP_KEY = "last_updated"
        const val SOURCE_CURRENCY_KEY = "source_currency"
        const val TARGET_CURRENCY_KEY = "target_currency"

        val defaultSourceCurrency = CurrencyCode.USD.name
        val defaultTargetCurrency = CurrencyCode.IDR.name
    }

    private val flowSettings = (settings as ObservableSettings).toFlowSettings()

    override suspend fun saveLastUpdated(lastUpdated: String) {
        flowSettings.putLong(
            key = TIMESTAMP_KEY,
            value = Instant.parse(lastUpdated).toEpochMilliseconds()
        )
    }

    override suspend fun isDataFresh(currentTimeStamp: Long): Boolean {
        val savedSettings = flowSettings.getLong(
            key = TIMESTAMP_KEY,
            defaultValue = 0L
        )

        return if (savedSettings != 0L) {
            val currentInstant = Instant.fromEpochMilliseconds(currentTimeStamp)
            val savedTime = Instant.fromEpochMilliseconds(savedSettings)

            val currentDateTime = currentInstant
                .toLocalDateTime(TimeZone.currentSystemDefault())

            val savedDateTime = savedTime
                .toLocalDateTime(TimeZone.currentSystemDefault())

            val daysDiffrence = currentDateTime.date.dayOfYear - savedDateTime.date.dayOfYear
            daysDiffrence < 1
        } else false
    }

    override suspend fun saveSourceCurrencyCode(code: String) {
        flowSettings.putString(
            key = SOURCE_CURRENCY_KEY,
            value = code
        )
    }

    override suspend fun saveTargetCurrencyCode(code: String) {
        flowSettings.putString(
            key = TARGET_CURRENCY_KEY,
            value = code
        )
    }

    override fun readSourceCurrencyCode(): Flow<CurrencyCode> {
        return flowSettings.getStringFlow(
            key = SOURCE_CURRENCY_KEY,
            defaultValue = defaultSourceCurrency
        ).map { CurrencyCode.valueOf(it) }
    }

    override fun readTargetCurrencyCode(): Flow<CurrencyCode> {
        return flowSettings.getStringFlow(
            key = TARGET_CURRENCY_KEY,
            defaultValue = defaultTargetCurrency
        ).map { CurrencyCode.valueOf(it) }
    }
}