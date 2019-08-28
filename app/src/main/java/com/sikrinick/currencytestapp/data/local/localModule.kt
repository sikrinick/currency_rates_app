package com.sikrinick.currencytestapp.data.local

import com.sikrinick.currencytestapp.data.local.db.AppDatabase
import com.sikrinick.currencytestapp.data.local.db.CurrencyModelsMapper
import com.sikrinick.currencytestapp.data.local.db.CurrencyRatesDb
import com.sikrinick.currencytestapp.data.platform.NetworkInfoProvider
import com.sikrinick.currencytestapp.data.platform.NetworkStateRepository
import org.koin.dsl.module

val localModule = module {

    single { AppDatabase.create(get()) }
    single { get<AppDatabase>().currencyRates() }


    single { CurrencyModelsMapper(get()) }
    single { CurrencyRatesDb(get(), get()) }


    single { NetworkInfoProvider(get()) }
    single { NetworkStateRepository(get()) }


}