package com.riezki.cmpcurrencyapp.data.di

import com.riezki.cmpcurrencyapp.data.local.MongoRepositoryImpl
import com.riezki.cmpcurrencyapp.data.local.PreferencesRepositoryImpl
import com.riezki.cmpcurrencyapp.data.remote.api.CurrentApiServiceImpl
import com.riezki.cmpcurrencyapp.domain.CurrencyApiService
import com.riezki.cmpcurrencyapp.domain.MongoRepository
import com.riezki.cmpcurrencyapp.domain.PreferencesRepository
import com.riezki.cmpcurrencyapp.presenter.screen.HomeViewModel
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * @author riezky maisyar
 */

val appModule = module {
    single { Settings() }
    singleOf(::MongoRepositoryImpl) { bind<MongoRepository>() }
    single<PreferencesRepository> { PreferencesRepositoryImpl(settings = get()) }
    single<CurrencyApiService> { CurrentApiServiceImpl(preferences = get()) }
    single { HomeViewModel(preference = get(), currencyApiService = get(), mongoRepository = get()) }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}