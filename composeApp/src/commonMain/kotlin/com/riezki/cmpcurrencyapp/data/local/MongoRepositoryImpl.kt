package com.riezki.cmpcurrencyapp.data.local

import com.riezki.cmpcurrencyapp.data.local.model.CurrencyEntity
import com.riezki.cmpcurrencyapp.data.remote.dto.Currency
import com.riezki.cmpcurrencyapp.domain.MongoRepository
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState
import com.riezki.cmpcurrencyapp.domain.data.utils.toCurrency
import com.riezki.cmpcurrencyapp.domain.data.utils.toCurrencyEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * @author riezky maisyar
 */

class MongoRepositoryImpl : MongoRepository {
    private var realmDb: Realm? = null

    override fun configureRealmDb() {
        if (realmDb == null || realmDb?.isClosed() == true) {
            val config = RealmConfiguration.Builder(
                schema = setOf(CurrencyEntity::class)
            ).compactOnLaunch()
                .build()
            realmDb = Realm.open(config)
        }
    }

    override suspend fun insertCurrencyData(entity: Currency) {
        realmDb?.write { copyToRealm(entity.toCurrencyEntity()) }
    }

    override fun readCurrencyData(): Flow<RequestState<List<Currency>>> {
        return realmDb?.query(CurrencyEntity::class)
            ?.asFlow()
            ?.map { results ->
                val currency = results.list.map { it.toCurrency() }
                RequestState.Success(data = currency)
            } ?: flowOf(RequestState.Error(message = "Realm is not initialized"))
    }

    override suspend fun cleanUp() {
        realmDb?.write {
            val currencyCollection = this.query<CurrencyEntity>()
            delete(currencyCollection)
        }
    }

    init {
        configureRealmDb()
    }
}