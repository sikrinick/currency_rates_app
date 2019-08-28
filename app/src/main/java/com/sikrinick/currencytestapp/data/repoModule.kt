package com.sikrinick.currencytestapp.data

import com.sikrinick.currencytestapp.data.gson.gson
import com.sikrinick.currencytestapp.data.local.localModule
import com.sikrinick.currencytestapp.data.remote.remoteModule
import org.koin.dsl.module

private val adapterModule = module {
    single { gson }

    single { CurrencyRatesRepository(get(), get()) }
}

val repoModule = adapterModule + localModule + remoteModule
